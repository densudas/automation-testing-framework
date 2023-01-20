package io.github.densudas.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import io.github.densudas.ControlSort;
import io.github.densudas.Locator;
import io.github.densudas.controls.BaseControl;
import io.github.densudas.controls.ControlType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

public class ControlsStorage {

  private static final String CONTROL_STORAGE_FILE_PATH = String.join(Utils.FILE_SEPARATOR,
      Utils.USER_DIR, "src", "main", "resources", "controls.json");
  private static final Map<Long, ControlsStorage> CONTROL_STORAGES_LIST = new LinkedTreeMap<>();
  private final String prefix = "random";
  private Map<Object, Object> controls = new LinkedTreeMap<>();
  private boolean isStorageLoaded;

  @Nullable
  public static Locator getLocatorFromStorage(String location, ControlType type, String name)
      throws Exception {
    ControlsStorage controlsStorage = getControlsFromStorage();
    if (controlsStorage.controls == null || controlsStorage.controls.isEmpty()) return null;

    String[] pageObjects = location.split("\\.");
    return getLocatorFromStorage(controlsStorage.controls, pageObjects, location, type, name);
  }

  @Nullable
  private static Locator getLocatorFromStorage(
      Map<Object, Object> controls,
      String[] pageObjects,
      String location,
      ControlType type,
      String name) {

    String pageObject = pageObjects[0];

    if (controls.containsKey(pageObject)) {
      if (pageObjects.length == 1) {
        return getLocatorFromStorage(
            (Map<Object, List<Map<Object, Object>>>) controls.get(pageObject),
            location,
            type,
            name);
      } else {
        return getLocatorFromStorage(
            (Map<Object, Object>) controls.get(pageObject),
            Arrays.copyOfRange(pageObjects, 1, pageObjects.length),
            location,
            type,
            name);
      }
    }
    return null;
  }

  @Nullable
  private static Locator getLocatorFromStorage(
      Map<Object, List<Map<Object, Object>>> pageControls,
      String location,
      ControlType type,
      String name) {

    if (pageControls != null && !pageControls.isEmpty()) {
      List<Map<Object, Object>> controls = pageControls.get(type.toString());
      if (controls != null) {
        for (Map<Object, Object> control : controls) {
          if (control.get("name").equals(name)
              && control.get("type").equals(type)
              && control.get("location").equals(location)) {
            return getLocatorFromStorage(control);
          }
        }
      }
    }
    return null;
  }

  public static void addControlToStorage(BaseControl control) throws Exception {
    if (!control.getSaveToControlsStorage()) return;
    ControlsStorage controlsFromStorage = getControlsFromStorage();

    String location = control.getLocation();

    Map<Object, Object> newElement = new LinkedTreeMap<>();
    newElement.put("control_type", control.getControlType());
    newElement.put("control_sort", control.getLocator().getControlSort());
    newElement.put("location", location);
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

    putToControlsStructure(controlsFromStorage.controls, pageObjects, newElement);
  }

  private static void putToControlsStructure(Map<Object, Object> controls, String[] pageObjects,
                                             Map<Object, Object> newElement) {
    String pageObject = pageObjects[0];

    if (controls.containsKey(pageObject)) {
      if (pageObjects.length == 1) {
        putToControlsStructure(
            (Map<Object, List<Map<Object, Object>>>) controls.get(pageObject), newElement);
      } else {
        putToControlsStructure(
            (Map<Object, Object>) controls.get(pageObject),
            Arrays.copyOfRange(pageObjects, 1, pageObjects.length),
            newElement);
      }

    } else {

      if (pageObjects.length == 1) {
        Map<Object, List<Map<Object, Object>>> elementsByType = new LinkedTreeMap<>();

        elementsByType.put(newElement.get("control_type"), new ArrayList<>());
        elementsByType.get(newElement.get("control_type")).add(newElement);

        controls.put(pageObject, elementsByType);

      } else {
        controls.put(pageObject, new LinkedTreeMap<>());
        putToControlsStructure(
            (Map<Object, Object>) controls.get(pageObject),
            Arrays.copyOfRange(pageObjects, 1, pageObjects.length),
            newElement);
      }
    }
  }

  private static void putToControlsStructure(Map<Object, List<Map<Object, Object>>> pageObject,
                                             Map<Object, Object> newElement) {

    List<Map<Object, Object>> elementsOfTheType = pageObject.get(newElement.get("control_type"));

    if (elementsOfTheType != null) {

      boolean updated = false;
      for (int i = 0; i < elementsOfTheType.size(); i++) {
        Map<Object, Object> element = elementsOfTheType.get(i);
        if (element.get("name").equals(newElement.get("name"))
            && element.get("location").equals(newElement.get("location"))) {
          elementsOfTheType.set(i, newElement);
          updated = true;
          break;
        }
      }
      if (!updated) elementsOfTheType.add(newElement);

    } else {
      pageObject.put(newElement.get("control_type"), new ArrayList<>());
      pageObject.get(newElement.get("control_type")).add(newElement);
    }
  }

  public static void writeStoragesToFile() throws Exception {
    ControlsStorage controlsStorage = new ControlsStorage();
    if (!CONTROL_STORAGES_LIST.isEmpty()) {
      for (ControlsStorage controlsStorageOfThread : CONTROL_STORAGES_LIST.values()) {
        if (!(controlsStorageOfThread.controls == null || controlsStorageOfThread.controls.isEmpty())
            && controlsStorageOfThread.isStorageLoaded) {
          controlsStorage.controls =
              mergeStorages(controlsStorage.controls, controlsStorageOfThread.controls);
        }
      }
      String prettyJsonString = getPrettyJsonString(controlsStorage.controls);
      Path filePath = Paths.get(CONTROL_STORAGE_FILE_PATH);
      Files.write(filePath, prettyJsonString.getBytes());
    }
  }

  private static Locator getLocatorFromStorage(Map<Object, Object> control) {
    ControlType controlType = Enum.valueOf(ControlType.class, ((String) control.get("control_type"))
        .toUpperCase());
    By by = new LocatorMatcher((String) control.get("locator_type"), (String) control.get("locator"))
        .buildBy();
    ControlSort controlSort = controlType.defineControlSort((String) control.get("control_sort"));
    Locator parentLocator = getLocatorFromStorage((Map<Object, Object>) control.get("parent_locator"));

    return new Locator(controlSort, by)
        .setHasShadowRoot((boolean) control.get("shadow_root"))
        .setSearchFromRootNode((boolean) control.get("search_from_root_node"))
        .setParentLocator(parentLocator);
  }

  private static ControlsStorage getControlsFromStorage() throws Exception {
    long currentThreadId = Thread.currentThread().getId();
    if (CONTROL_STORAGES_LIST.get(currentThreadId) == null) {
      CONTROL_STORAGES_LIST.put(currentThreadId, new ControlsStorage());
    }
    return CONTROL_STORAGES_LIST.get(currentThreadId).loadStorageFromFile();
  }

  private static String getPrettyJsonString(Map<Object, Object> json) {
    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    return gson.toJson(json).replace("\\\\", "\\");
  }

  private static Map<Object, Object> mergeStorages(
      Map<Object, Object> currentControlStorage, Map<Object, Object> externalControlStorage) {

    if (currentControlStorage.isEmpty()) {
      currentControlStorage = externalControlStorage;
      return currentControlStorage;
    }

    for (Object key : externalControlStorage.keySet()) {

      if (currentControlStorage.containsKey(key)) {
        if (key instanceof ControlType) {
          List<Map<Object, Object>> elements =
              (List<Map<Object, Object>>) currentControlStorage.get(key);
          List<Map<Object, Object>> extElements =
              (List<Map<Object, Object>>) externalControlStorage.get(key);
          for (Map<Object, Object> extElement : extElements) {
            boolean updated = false;

            for (int i = 0; i < elements.size(); i++) {
              Instant dateTime = Instant.parse((String) elements.get(i).get("date_time_created_utc"));
              ControlType controlType = (ControlType) elements.get(i).get("control_type");
              ControlSort controlSort = (ControlSort) elements.get(i).get("control_sort");
              String location = (String) elements.get(i).get("location");
              String name = (String) elements.get(i).get("name");

              if (extElement.get("control_type").equals(controlType)
                  && extElement.get("control_sort").equals(controlSort)
                  && extElement.get("location").equals(location)
                  && extElement.get("name").equals(name)) {

                if (Instant.parse((String) elements.get(i)
                    .get("date_time_created_utc")).compareTo(dateTime) >= 0) {
                  elements.set(i, extElement);
                  updated = true;
                  break;
                }
              } else {
                elements.set(i, extElement);
                updated = true;
                break;
              }
            }

            if (!updated) elements.add(extElement);
          }
        } else {
          mergeStorages(
              (Map<Object, Object>) currentControlStorage.get(key),
              (Map<Object, Object>) externalControlStorage.get(key));
        }

      } else {
        currentControlStorage.put(key, externalControlStorage.get(key));
      }
    }

    return currentControlStorage;
  }

  private ControlsStorage loadStorageFromFile() throws Exception {
    if (!isStorageLoaded) {
      Path filePath = Paths.get(CONTROL_STORAGE_FILE_PATH);

      if (Files.exists(filePath)) {
        Scanner sc = new Scanner(filePath);
        if (sc.hasNext()) {
          String str = sc.useDelimiter("\\Z").next();
          Map<Object, Object> controlStorage = new Gson().fromJson(str, Map.class);
          if (controlStorage == null) return this;
          controls = controlStorage;
        }
      }

      isStorageLoaded = true;
    }
    return this;
  }

  public void removePageFromStorage(String pageName) {
    try {
      StringBuilder id = new StringBuilder();
      for (String s : pageName.split(" ")) {
        id.append(StringUtils.capitalize(s));
      }
      List<Object> keys =
          controls.keySet().stream().filter(key -> ((String) key).contains(id)).toList();
      keys.forEach(controls::remove);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  public void removeTemporaryControls() {
    List<Object> keys =
        controls.keySet().stream()
            .filter(pageName -> ((String) pageName).contains(prefix))
            .toList();

    keys.forEach(controls::remove);

    for (Object pageName : controls.keySet()) {
      Map<Object, List<Map<Object, Object>>> page =
          (Map<Object, List<Map<Object, Object>>>) controls.get(pageName);

      for (Object controlType : page.keySet()) {
        List<Map<Object, Object>> controls = page.get(controlType);

        List<Map<Object, Object>> newList = new ArrayList<>();
        controls.parallelStream()
            .filter(control -> ((String) control.get("location")).contains(prefix)
                || ((String) control.get("name")).contains(prefix))
            .forEach(newList::add);
        controls.removeAll(newList);
      }
    }
  }
}
