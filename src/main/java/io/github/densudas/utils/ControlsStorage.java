package io.github.densudas.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import io.github.densudas.ControlSort;
import io.github.densudas.Locator;
import io.github.densudas.controls.BaseControl;
import io.github.densudas.controls.ControlType;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ControlsStorage {

  private static final String CONTROL_STORAGE_FILE_PATH =
      String.join(
          Utils.FILE_SEPARATOR,
          Utils.USER_DIR
              + "src"
              + "main"
              + "java"
              + "io"
              + "github"
              + "densudas"
              + "utils"
              + "controls.json");

  private String prefix = "random";
  private Map<String, Map> controls = new LinkedTreeMap<>();
  private boolean isStorageLoaded;
  private static Map<Long, ControlsStorage> controlStoragesList = new HashMap<>();

  public static Locator getLocatorFromStorage(String location, ControlType type, String name)
      throws Exception {
    ControlsStorage controlsStorage = getControlsFromStorage();
    if (controlsStorage.controls == null || controlsStorage.controls.isEmpty()) return null;

    Map<?, ?> pageControls = controlsStorage.controls.get(location);
    if (pageControls != null) {
      ArrayList<?> controls = (ArrayList<?>) pageControls.get(type.toString());
      if (controls != null) {
        for (LinkedTreeMap<?, ?> control : (ArrayList<LinkedTreeMap<?, ?>>) controls) {
          if (control.get("name").equals(name) && control.get("location").equals(location)) {
            return controlsStorage.getLocatorFromStorage(control);
          }
        }
      }
    }
    return null;
  }

  public static void addControlToStorage(BaseControl control) throws Exception {
    if (!control.getSaveToControlsStorage()) return;
    ControlsStorage controlsStorage = getControlsFromStorage();

    String typeString = control.getControlType().getName();
    String location = control.getLocation();

    Map<String, Object> newElement = new LinkedTreeMap<>();
    newElement.put("control_type", control.getControlType().getName());
    newElement.put("control_sort", control.getLocator().getControlSort().toString());
    newElement.put("location", location);
    newElement.put("type", typeString);
    newElement.put("name", control.getName());

    newElement.put("date_time_created_utc", Instant.now().toString());

    LocatorMatcher locatorMatcher = new LocatorMatcher(control.getLocator().getBy());
    locatorMatcher.formatWithName(control.getName());
    newElement.put("locator_type", locatorMatcher.getLocatorType());
    newElement.put("locator", locatorMatcher.getLocator());

    newElement.put("shadow_root", control.getShadowRoot());
    newElement.put("search_from_root_node", control.getSearchFromRootNode());

    newElement.put("parent_locator", control.getLocator().getParentLocator());

    String[] pageObjects = location.split("\\.");

    putToControlsStructure(controlsStorage.controls, pageObjects, newElement);
  }

  private static void putToControlsStructure(
      Map<String, Map> controls, String[] pageObjects, Map<String, Object> newElement) {

    String pageObject = pageObjects[0];

    if (controls.containsKey(pageObject)) {
      if (pageObjects.length == 1) {
        putToControlsStructure(
            (LinkedTreeMap<String, ArrayList<?>>) controls.get(pageObject),
            (LinkedTreeMap<String, Object>) newElement);
      } else {
        putToControlsStructure(
            (Map<String, Map>) controls.get(pageObject),
            Arrays.copyOfRange(pageObjects, 1, pageObjects.length),
            newElement);
      }

    } else {

      if (pageObjects.length == 1) {
        Map<String, List<Map<String, Object>>> elementsByType = new LinkedTreeMap<>();

        elementsByType.put(
            (String) newElement.get("type"),
            new ArrayList<>() {
              {
                add(newElement);
              }
            });

        controls.put(pageObject, elementsByType);

      } else {
        controls.put(pageObject, new LinkedTreeMap<>());
        putToControlsStructure(
            controls.get(pageObject),
            Arrays.copyOfRange(pageObjects, 1, pageObjects.length),
            newElement);
      }
    }
  }

  private static void putToControlsStructure(
      LinkedTreeMap<String, ArrayList<?>> pageObject, LinkedTreeMap<String, Object> newElement) {

    List<LinkedTreeMap<?, ?>> elementsOfTheType =
        (ArrayList<LinkedTreeMap<?, ?>>) pageObject.get(newElement.get("type"));

    if (elementsOfTheType != null) {

      boolean updated = false;
      for (int i = 0; i < elementsOfTheType.size(); i++) {
        LinkedTreeMap<?, ?> element = elementsOfTheType.get(i);
        if (element.get("name").equals(newElement.get("name"))
            && element.get("location").equals(newElement.get("location"))) {
          elementsOfTheType.set(i, newElement);
          updated = true;
          break;
        }
      }
      if (!updated) {
        elementsOfTheType.add(newElement);
      }

    } else {

      pageObject.put(
          (String) newElement.get("type"),
          new ArrayList<>() {
            {
              add(newElement);
            }
          });
    }
  }

  public static void writeStorageToFile() throws Exception {
    ControlsStorage controlsStorage = getControlsFromStorage();
    if (!(controlsStorage.controls == null || controlsStorage.controls.isEmpty())
        && controlsStorage.isStorageLoaded) {
      String prettyJsonString = getPrettyJsonString((LinkedTreeMap<?, ?>) controlsStorage.controls);
      Path filePath = Paths.get(CONTROL_STORAGE_FILE_PATH);
      Files.write(filePath, prettyJsonString.getBytes());
    }
  }

  private Locator getLocatorFromStorage(LinkedTreeMap<?, ?> control) {
    ControlType controlType =
        Enum.valueOf(ControlType.class, ((String) control.get("control_type")).toUpperCase());
    By by =
        new LocatorMatcher((String) control.get("locator_type"), (String) control.get("locator"))
            .buildBy();
    ControlSort controlSort = controlType.defineControlSort((String) control.get("control_sort"));
    Locator parentLocator =
        getLocatorFromStorage((LinkedTreeMap<?, ?>) control.get("parent_locator"));

    return new Locator(controlSort, by)
        .setHasShadowRoot((boolean) control.get("shadow_root"))
        .setSearchFromRootNode((boolean) control.get("search_from_root_node"))
        .setParentLocator(parentLocator);
  }

  private static ControlsStorage getControlsFromStorage() throws Exception {
    if (controlStoragesList.get(Thread.currentThread().getId()) == null) {
      controlStoragesList.put(Thread.currentThread().getId(), new ControlsStorage());
    }
    ControlsStorage controlsStorage = controlStoragesList.get(Thread.currentThread().getId());
    controlsStorage.loadStorageFromFile();
    return controlsStorage;
  }

  private void loadStorageFromFile() throws Exception {
    if (!isStorageLoaded) {
      Path filePath = Paths.get(CONTROL_STORAGE_FILE_PATH);

      if (Files.exists(filePath)) {
        Scanner sc = new Scanner(filePath);
        if (sc.hasNext()) {
          String str = sc.useDelimiter("\\Z").next();
          LinkedTreeMap<?, ?> controlStorage = new Gson().fromJson(str, LinkedTreeMap.class);
          if (controlStorage == null) return;
          controls = (LinkedTreeMap<String, Map>) controlStorage;
        }
      }

      isStorageLoaded = true;
    }
  }

  private static String getPrettyJsonString(LinkedTreeMap<?, ?> json) {
    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    return (gson.toJson(json)).replace("\\\\", "\\");
  }

  public void removePageFromStorage(String pageName) {
    try {
      StringBuilder id = new StringBuilder();
      for (String s : pageName.split(" ")) {
        id.append(StringUtils.capitalize(s));
      }
      ArrayList<String> keys =
          (ArrayList<String>) controls.keySet().stream().filter(key -> key.contains(id)).toList();
      keys.forEach(controls::remove);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  public void removeTemporaryControls() {
    ArrayList<String> keys =
        (ArrayList<String>)
            controls.keySet().stream().filter(pageName -> pageName.contains(prefix)).toList();

    keys.forEach(controls::remove);

    controls
        .keySet()
        .forEach(
            pageName -> {
              LinkedTreeMap<String, ArrayList<?>> page =
                  (LinkedTreeMap<String, ArrayList<?>>) controls.get(pageName);

              page.keySet()
                  .forEach(
                      controlType -> {
                        ArrayList<LinkedTreeMap<?, ?>> controls =
                            (ArrayList<LinkedTreeMap<?, ?>>) page.get(controlType);

                        ArrayList<LinkedTreeMap<?, ?>> newList = new ArrayList<>();
                        controls.parallelStream()
                            .filter(
                                control ->
                                    ((String) (control).get("location")).contains(prefix)
                                        || ((String) (control).get("name")).contains(prefix))
                            .forEach(newList::add);
                        controls.removeAll(newList);
                      });
            });
  }

  public static LinkedTreeMap<String, LinkedTreeMap<String, ?>> mergeStorages(
      LinkedTreeMap<String, LinkedTreeMap<String, ?>> externalControlStorage,
      LinkedTreeMap<String, LinkedTreeMap<String, ?>> currentControlStorage) {
    for (String key : new ArrayList<>(currentControlStorage.keySet())) {
      if (externalControlStorage.containsKey(key)) {
        LinkedTreeMap<String, ArrayList<?>> pageExternal =
            (LinkedTreeMap<String, ArrayList<?>>) externalControlStorage.get(key);
        LinkedTreeMap<String, ArrayList<?>> page =
            (LinkedTreeMap<String, ArrayList<?>>) currentControlStorage.get(key);
        for (String keyControls : new ArrayList<>(page.keySet())) {
          if (pageExternal.containsKey(keyControls)) {
            ArrayList<LinkedTreeMap<String, ?>> controlsExternal =
                (ArrayList<LinkedTreeMap<String, ?>>) pageExternal.get(keyControls);
            ArrayList<LinkedTreeMap<String, ?>> controls =
                (ArrayList<LinkedTreeMap<String, ?>>) page.get(keyControls);

            for (LinkedTreeMap<String, ?> currentControl : controls) {
              boolean updated = false;
              for (int i = 0; i < controlsExternal.size(); i++) {
                if (controlsExternal.get(i).get("location").equals(currentControl.get("location"))
                    && controlsExternal.get(i).get("name").equals(currentControl.get("name"))) {
                  controlsExternal.set(i, currentControl);
                  updated = true;
                  break;
                }
              }
              if (!updated) {
                controlsExternal.add(currentControl);
              }
            }
          } else {
            pageExternal.put(keyControls, page.get(keyControls));
          }
        }
      } else {
        externalControlStorage.put(key, currentControlStorage.get(key));
      }
    }
    return externalControlStorage;
  }
}
