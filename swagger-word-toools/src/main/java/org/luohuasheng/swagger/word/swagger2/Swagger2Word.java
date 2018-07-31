package org.luohuasheng.swagger.word.swagger2;

import io.swagger.models.*;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import org.apache.commons.lang3.StringUtils;
import org.luohuasheng.swagger.tools.TemplateTools;
import org.luohuasheng.swagger.tools.ZipTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Swagger2Word {

    public static void exportDoc(Swagger swagger, String name, String outDir) {
        try {
            if (outDir == null || "".equals(outDir)) {
                outDir = "./";
            } else if (!outDir.endsWith("/")) {
                outDir = outDir + "/";
            }
            File outTempFile = File.createTempFile("temp", ".xml");
            TemplateTools.createFile(buildTemplateData(swagger), "template.ftl", outTempFile.getCanonicalPath());
            ZipInputStream zipInputStream = ZipTools.wrapZipInputStream(Swagger2Word.class.getClassLoader().getResourceAsStream("template.docx"));
            ZipOutputStream zipOutputStream = ZipTools.wrapZipOutputStream(new FileOutputStream(new File(outDir + name + ".docx")));
            String itemname = "word/document.xml";
            ZipTools.replaceItem(zipInputStream, zipOutputStream, itemname, new FileInputStream(new File(outTempFile.getCanonicalPath())));
            outTempFile.deleteOnExit();//程序退出时删除临时文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> buildTemplateData(Swagger swagger) {
        Map<String, Object> data = new HashMap<>(0);
        if (swagger != null) {
            Info info = swagger.getInfo();
            data.put("info", getInfo(info));
            data.put("tags", getTag(swagger));
        }

        return data;

    }

    private static List<Map<String, Object>> getTag(Swagger swagger) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Tag tag : swagger.getTags()) {
            Map<String, Object> data = new HashMap<>(0);
            data.put("name", tag.getName());
            if (!StringUtils.isEmpty(tag.getDescription())) {
                data.put("description", tag.getDescription());
            }
            buildPath(data, swagger);
            list.add(data);
        }
        return list;
    }

    private static void buildPath(Map<String, Object> data, Swagger swagger) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, Path> stringPathEntry : swagger.getPaths().entrySet()) {
            for (Map.Entry<HttpMethod, Operation> httpMethodOperationEntry : stringPathEntry.getValue().getOperationMap().entrySet()) {
                if (httpMethodOperationEntry.getValue().getTags().contains(data.get("name"))) {
                    Map<String, Object> dataEntry = new HashMap<>(0);
                    dataEntry.put("method", httpMethodOperationEntry.getKey().name());
                    dataEntry.put("name", httpMethodOperationEntry.getValue().getSummary());
                    dataEntry.put("description", httpMethodOperationEntry.getValue().getDescription());
                    dataEntry.put("url", stringPathEntry.getKey());
                    buildParameters(dataEntry, httpMethodOperationEntry.getValue().getParameters(), swagger.getDefinitions());
                    buildResponse(dataEntry, httpMethodOperationEntry.getValue().getResponses(), swagger.getDefinitions());

                    list.add(dataEntry);
                }
            }
        }
        data.put("paths", list);

    }

    private static void buildResponse(Map<String, Object> dataEntry, Map<String, Response> responses, Map<String, Model> definitions) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> responseMap = new HashMap<>(0);
        list.add(responseMap);
        if (responses == null || responses.get("200") == null) {
            return;
        }
        Property property = responses.get("200").getSchema();
        if (property == null) {
            return;
        } else if (property instanceof ArrayProperty) {
            responseMap.put("type", property.getType() + "<" + getPropertyType(((ArrayProperty) property).getItems()) + ">");
            if (((ArrayProperty) property).getItems() instanceof RefProperty) {
                buildRef(((RefProperty) ((ArrayProperty) property).getItems()).getSimpleRef(), list, "ROOT", definitions, new ArrayList<>());
            }
        } else {
            responseMap.put("type", getPropertyType(property));
            if (property instanceof RefProperty) {
                buildRef(((RefProperty) property).getSimpleRef(), list, "ROOT", definitions, new ArrayList<>());
            }
        }
        responseMap.put("required", property.getRequired());
        responseMap.put("name", "ROOT");
        responseMap.put("description", "虚拟节点,返回响应根节点");
        responseMap.put("parentName", "");
        dataEntry.put("responses", list);
    }

    private static String getPropertyType(Property property) {
        if (property instanceof RefProperty) {
            return ((RefProperty) property).getSimpleRef();
        } else {
            return property.getType();
        }
    }

    private static void buildParameters(Map<String, Object> dataEntry, List<Parameter> parameters, Map<String, Model> definitions) {
        List<String> definitionValues = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();

        for (Parameter parameter : parameters) {
            Map<String, Object> parameterMap = new HashMap<>(0);
            list.add(parameterMap);
            parameterMap.put("required", parameter.getRequired());
            parameterMap.put("name", parameter.getName());
            parameterMap.put("description", parameter.getDescription());
            parameterMap.put("queryType", parameter.getIn());
            parameterMap.put("parentName", "");

            if (parameter instanceof QueryParameter) {
                QueryParameter queryParameter = (QueryParameter) parameter;
                parameterMap.put("type", queryParameter.getType());
            } else if (parameter instanceof BodyParameter) {
                BodyParameter bodyParameter = (BodyParameter) parameter;
                Model model = bodyParameter.getSchema();
                String type = null;
                if (model instanceof ArrayModel) {
                    type = ((ArrayModel) model).getType();
                    if (((ArrayModel) model).getItems() != null) {
                        if (((ArrayModel) model).getItems() instanceof RefProperty) {
                            type += "<" + ((RefProperty) ((ArrayModel) model).getItems()).getSimpleRef() + ">";
                        } else {
                            type += "<" + ((ArrayModel) model).getItems().getType() + ">";

                        }
                    }
                } else if (model instanceof RefModel) {
                    type = ((RefModel) model).getSimpleRef();
                    buildRef((RefModel) model, list, parameter.getName(), definitions, definitionValues);
                } else if (model instanceof ModelImpl) {
                    type = ((ModelImpl) model).getType();
                } else {
                    parameterMap.put("type", "");
                }

                parameterMap.put("type", type);
            } else if (parameter instanceof FormParameter) {
                parameterMap.put("type", ((FormParameter) parameter).getType());
            } else if (parameter instanceof PathParameter) {
                parameterMap.put("type", ((PathParameter) parameter).getType());
            }
        }

        if (list.size() != 0) {
            dataEntry.put("parameters", list);
        }
    }

    private static void buildRef(String refName, List<Map<String, Object>> list, String name, Map<String, Model> definitions, List<String> definitionValues) {

        if (!definitionValues.contains(name)) {
            definitionValues.add(name);
            for (Map.Entry<String, Model> stringModelEntry : definitions.entrySet()) {
                if (stringModelEntry.getKey().equals(refName)) {
                    if (stringModelEntry.getValue().getProperties() == null) {
                        continue;
                    }
                    for (Map.Entry<String, Property> stringPropertyEntry : stringModelEntry.getValue().getProperties().entrySet()) {
                        Map<String, Object> parameterMap = new HashMap<>(0);
                        list.add(parameterMap);
                        parameterMap.put("required", stringPropertyEntry.getValue().getRequired());
                        parameterMap.put("name", stringPropertyEntry.getKey());
                        parameterMap.put("description", stringPropertyEntry.getValue().getDescription());
                        parameterMap.put("queryType", "");
                        parameterMap.put("parentName", name);
                        parameterMap.put("type", stringPropertyEntry.getValue().getType());
                        if (stringPropertyEntry.getValue() instanceof RefProperty) {
                            buildRef(((RefProperty) stringPropertyEntry.getValue()), list, stringPropertyEntry.getKey(), definitions, definitionValues);
                        } else if (stringPropertyEntry.getValue() instanceof ArrayProperty) {
                            parameterMap.put("type", parameterMap.get("type") + "<" + ((ArrayProperty) stringPropertyEntry.getValue()).getItems().getType() + ">");
                            if (((ArrayProperty) stringPropertyEntry.getValue()).getItems() instanceof RefProperty) {
                                buildRef(((RefProperty) ((ArrayProperty) stringPropertyEntry.getValue()).getItems()), list, stringPropertyEntry.getKey(), definitions, definitionValues);
                            }
                        }

                    }
                }
            }
        }
    }

    private static void buildRef(RefModel model, List<Map<String, Object>> list, String name, Map<String, Model> definitions, List<String> definitionValues) {
        buildRef(model.getSimpleRef(), list, name, definitions, definitionValues);

    }

    private static void buildRef(RefProperty property, List<Map<String, Object>> list, String name, Map<String, Model> definitions, List<String> definitionValues) {
        buildRef(property.getSimpleRef(), list, name, definitions, definitionValues);


    }


    private static Map<String, Object> getInfo(Info info) {
        Map<String, Object> data = new HashMap<>(0);
        if (!StringUtils.isEmpty(info.getTitle())) {
            data.put("title", info.getTitle());
        }
        if (!StringUtils.isEmpty(info.getDescription())) {
            data.put("description", info.getDescription());
        }
        return data;
    }
}
