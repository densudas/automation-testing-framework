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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ControlsStorage {

  public static final String CONTROLS_FOLDER_PATH_JSON =
      String.join(
          Utils.FILE_SEPARATOR,
          Utils.USER_DIR + "src" + "main" + "java" + "io" + "github" + "densudas" + "utils");

  public String pageID;
  public String prefix = "random";
  private LinkedTreeMap<String, LinkedTreeMap<String, ?>> controls = new LinkedTreeMap<>();
  private boolean isStorageLoaded;

  public void getStorageFromFile() throws Exception {
    if (!isStorageLoaded) {
      Path filePath = Paths.get(CONTROLS_FOLDER_PATH_JSON + Utils.FILE_SEPARATOR + "controls.json");

      if (Files.exists(filePath)) {
        Scanner sc = new Scanner(filePath);
        if (sc.hasNext()) {
          String str = sc.useDelimiter("\\Z").next();
          LinkedTreeMap<?, ?> controlStorage = new Gson().fromJson(str, LinkedTreeMap.class);
          if (controlStorage == null) return;
          controls = (LinkedTreeMap<String, LinkedTreeMap<String, ?>>) controlStorage;
        }
      }

      isStorageLoaded = true;
    }
  }

  private String getStorageVersionFromFile() throws Exception {
    Path filePath =
        Paths.get(CONTROLS_FOLDER_PATH_JSON + Utils.FILE_SEPARATOR + "storage_version.txt");
    return Files.exists(filePath) ? String.join(" ", Files.readAllLines(filePath)).trim() : null;
  }

  public void setStorageToFile() throws Exception {
    if (isStorageLoaded) {
      String prettyJsonString = getPrettyJsonString(controls);
      Path filePath = Paths.get(CONTROLS_FOLDER_PATH_JSON + Utils.FILE_SEPARATOR + "controls.json");
      Files.write(filePath, prettyJsonString.getBytes());
    }
  }

  public Locator getLocatorFromStorage(String location, ControlType type, String name) {
    if (controls == null || controls.isEmpty()) return null;

    LinkedTreeMap<?, ?> pageControls = controls.get(pageID);
    if (pageControls != null) {
      ArrayList<?> controls = (ArrayList<?>) pageControls.get(type.toString());
      if (controls != null) {
        for (LinkedTreeMap<?, ?> control : (ArrayList<LinkedTreeMap<?, ?>>) controls) {
          if (control.get("name").equals(name) && control.get("location").equals(location)) {
            return getLocatorFromStorage(control);
          }
        }
      }
    }
    return null;
  }

  public Locator getLocatorFromStorage(LinkedTreeMap<?, ?> control) {
    ControlType controlType =
        Enum.valueOf(ControlType.class, ((String) control.get("control_type")).toUpperCase());
    By by =
        new LocatorMatcher((String) control.get("locator_type"), (String) control.get("locator"))
            .getBy();
    ControlSort controlSort = controlType.defineControlSort((String) control.get("control_sort"));
    Locator parentLocator =
        getLocatorFromStorage((LinkedTreeMap<?, ?>) control.get("parent_locator"));

    return new Locator(controlSort, by)
        .setHasShadowRoot((boolean) control.get("shadow_root"))
        .setSearchFromRootNode((boolean) control.get("search_from_root_node"))
        .setParentLocator(parentLocator);
  }

  public void setControlToStorage(BaseControl control) {
    if (!control.getSaveToControlsStorage()) return;

    String typeString = control.getControlType().getName();
    String location = control.getLocation();

    LinkedTreeMap<String, Object> newElement = new LinkedTreeMap<>();
    newElement.put("control_type", control.getControlType().getName());
    newElement.put("control_sort", control.getLocator().getControlSort().toString());
    newElement.put("location", location);
    newElement.put("name", control.getName());

    LocatorMatcher locatorMatcher = new LocatorMatcher(control.getLocator().getBy());
    newElement.put("locator_type", locatorMatcher.getLocatorType());
    newElement.put("locator", locatorMatcher.getLocator());

    newElement.put("shadow_root", control.getShadowRoot());
    newElement.put("search_from_root_node", control.getSearchFromRootNode());

    newElement.put("parent_locator", control.getLocator().getParentLocator());

    ArrayList<LinkedTreeMap<?, ?>> ra_newElements = new ArrayList<>();
    ra_newElements.add(newElement);

    if (controls.containsKey(pageID)) {
      LinkedTreeMap<String, ArrayList<?>> pageID_structure =
          (LinkedTreeMap<String, ArrayList<?>>) controls.get(pageID);
      if (pageID_structure.containsKey(typeString)) {
        List<LinkedTreeMap<?, ?>> controlsType_structure =
            (ArrayList<LinkedTreeMap<?, ?>>) pageID_structure.get(typeString);
        boolean updated = false;
        for (int i = 0; i < controlsType_structure.size(); i++) {
          LinkedTreeMap<?, ?> element = controlsType_structure.get(i);
          // pageID and control type structures are presented
          // => replace an existing control in storage with a new data
          if (element.get("name").equals(control.getName())
              && element.get("location").equals(location)) {
            controlsType_structure.set(i, newElement);
            updated = true;
            break;
          }
        }
        if (!updated) {
          controlsType_structure.add(newElement);
        }
      } else {
        pageID_structure.put(typeString, ra_newElements);
      }
    } else {
      LinkedTreeMap<String, List<?>> pageID_newStructure = new LinkedTreeMap<>();
      pageID_newStructure.put(typeString, ra_newElements);
      controls.put(pageID, pageID_newStructure);
    }
  }

  private String getPrettyJsonString(LinkedTreeMap<?, ?> json) {
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
          (ArrayList<String>)
              controls.keySet().stream()
                  .filter(key -> key.contains(id))
                  .collect(Collectors.toList());
      keys.forEach(controls::remove);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  public void removeTemporaryControls() {
    ArrayList<String> keys =
        (ArrayList<String>)
            controls.keySet().stream()
                .filter(pageName -> pageName.contains(prefix))
                .collect(Collectors.toList());

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
