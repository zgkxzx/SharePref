package com.zgkxzx.shareprefprocess;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @Created zgkxzx
 * @Date 2018/5/19.
 * @Description
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class SPrefProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;
    private String newClassName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(SharePref.class.getCanonicalName());
        types.add(Table.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.print(roundEnvironment);
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Table.class);
        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element
                    .getEnclosingElement();

            VariableElement variableElement = (VariableElement) element;
            String fullClassName = typeElement.getQualifiedName().toString();
            String className = typeElement.getSimpleName().toString();

        }
        for (Element element : roundEnvironment.getElementsAnnotatedWith(SharePref.class)) {
            //target相同只能强转。不同使用getEnclosingElement
            //ExecutableElement executableElement = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) element
                    .getEnclosingElement();

            VariableElement variableElement = (VariableElement) element;

            PackageElement packageElement = elementUtils.getPackageOf(typeElement);


            List<ClassInfo> classInfos = new ArrayList<>();

            List<? extends Element> allMembers = elementUtils.getAllMembers(typeElement);
            for (Element member : allMembers) {
                SharePref item = member.getAnnotation(SharePref.class);
                if (item != null) {
                    classInfos.add(new ClassInfo(item.value(), member.getSimpleName().toString(), member.asType().toString()));
                }
            }

            String fullClassName = typeElement.getQualifiedName().toString();
            String className = typeElement.getSimpleName().toString();
            String packageName = packageElement.getQualifiedName().toString();

            newClassName = className + "SDO";

            String sharePrefUtils = getClassProxyInfo(packageName, className, classInfos);

            writeCode(sharePrefUtils, packageName + "." + newClassName, variableElement);

        }
        return true;
    }

    private void writeCode(String contect, String classFullName, Element typeElement) {
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                    classFullName);
            Writer writer = jfo.openWriter();
            writer.write(contect);
            writer.flush();
            writer.close();

        } catch (IOException e) {
//            error(typeElement,
//                    "Unable to write injector for type %s: %s",
//                    typeElement, e.getMessage());
        } catch (Exception e) {
//            error(typeElement,
//                    "The use of irregular %s: %s",
//                    typeElement, e.getMessage());
        }


    }

    /**
     * first upperCase
     *
     * @param name
     * @return
     */
    public String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;

    }

    private String getClassProxyInfo(String packageName, String proxyClassName, List<ClassInfo> classInfos) {
        String prefix = FormatUtil.humpToUnderline(proxyClassName) + "_";

        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code from SharePrefUtil. Do not modify!\n");
        builder.append("/**\n")
                .append("* @email: zgkxzx@163.com\n")
                .append(line("* @date: %s\n", FormatUtil.date2StrFormat(new Date(), FormatUtil.FORMAT_DATE_TIME_02)))
                .append(line("* @description: SharedPreferences for %s \n", proxyClassName))
                .append("*/\n");
        builder.append(line("package %s;\n\n", packageName));

        builder.append("import android.view.View;\n");
        builder.append("import android.content.Context;\n");
        builder.append("import com.zgkxzx.sharepref.SharePrefUtil;\n");
        builder.append('\n');

        builder.append(line("public class %s", newClassName));
        builder.append(" {\n");

        for (ClassInfo classInfo : classInfos) {
            //put 3 paras
            builder.append(line("public static void put%s(Context context,%s %s,String suffixTag){\n",
                    captureName(classInfo.getFieldName()),
                    classInfo.getTypeName(),
                    classInfo.getFieldName()));


            if (str2Default(classInfo.getTypeName()) != null) {
                builder.append(line("String keyName = \"%s\";\n", prefix + classInfo.getFieldName()))
                        .append("if(suffixTag==null||\"\".equals(suffixTag)){\n" +
                                " }else\n" +
                                " {\n" +
                                "      keyName = keyName+\"_\"+suffixTag;\n" +
                                " }");
                builder.append(line(" SharePrefUtil.put(context,keyName,%s);}", classInfo.getFieldName()));
            } else {
                builder.append(line(" %sSDO.put(context,%s ,suffixTag);}", classInfo.getTypeName(),classInfo.getFieldName()));
            }
            //put 2 paras
            builder.append(line("public static void put%s(Context context,%s %s){\n",
                    captureName(classInfo.getFieldName()),
                    classInfo.getTypeName(),
                    classInfo.getFieldName()));

            builder.append(line("  put%s(context,%s,null); }", captureName(classInfo.getFieldName()), classInfo.getFieldName()));

            //get 3 params
            if (str2Default(classInfo.getTypeName()) != null) {
                builder.append(line("public static %s get%s(Context context,%s defaultValue,String suffixTag){", classInfo.getTypeName(), captureName(classInfo.getFieldName()), classInfo.getTypeName()));
                builder.append(line("String keyName = \"%s\";\n", prefix + classInfo.getFieldName()))
                        .append("if(suffixTag==null||\"\".equals(suffixTag)){\n" +
                                " }else\n" +
                                " {\n" +
                                "      keyName = keyName+\"_\"+suffixTag;\n" +
                                " }");
                builder.append(line("return (%s) SharePrefUtil.get(context,keyName,defaultValue);}", classInfo.getTypeName()));
            } else {
                builder.append(line("public static %s get%s(Context context,String suffixTag){", classInfo.getTypeName(), captureName(classInfo.getFieldName())))
                    .append(line("return (%s) %sSDO.get(context,suffixTag);}", classInfo.getTypeName(), classInfo.getTypeName()));
            }
            // get 2 params
            if (str2Default(classInfo.getTypeName()) != null) {
                builder.append(line("public static %s get%s(Context context,%s %s){\n",
                        classInfo.getTypeName(),
                        captureName(classInfo.getFieldName()),
                        classInfo.getTypeName(),
                        classInfo.getFieldName()));

                builder.append(line("return  get%s(context,%s,null); }", captureName(classInfo.getFieldName()), classInfo.getFieldName()));

                builder.append(line("public static %s get%s(Context context){\n",
                        classInfo.getTypeName(),
                        captureName(classInfo.getFieldName())));

                builder.append(line("return  get%s(context,%s); }", captureName(classInfo.getFieldName()), getDefaultValue(classInfo.getTypeName(), classInfo.getValue())));
            }else{
                builder.append(line("public static %s get%s(Context context){\n",
                        classInfo.getTypeName(),
                        captureName(classInfo.getFieldName())));

                builder.append(line("return  get%s(context,null); }", captureName(classInfo.getFieldName())));
            }


        }
        // get for class
        builder.append(line("public static %s get(Context context,String suffixTag){\n", proxyClassName));
        builder.append(line("%s obj = new %s();", proxyClassName, proxyClassName));

        for (ClassInfo classInfo : classInfos) {
            if (str2Default(classInfo.getTypeName()) != null) {
                builder.append(line("obj.%s = get%s(context,%s,suffixTag);", classInfo.getFieldName(), captureName(classInfo.getFieldName()), getDefaultValue(classInfo.getTypeName(), classInfo.getValue())));
            } else {
                builder.append(line("obj.%s = get%s(context,suffixTag);", classInfo.getFieldName(), captureName(classInfo.getFieldName())));
            }
        }
        builder.append(" return obj;}\n");

        builder.append(line("public static %s get(Context context){\n", proxyClassName));
        builder.append(" return get(context,null);}\n");

        //set for class
        builder.append(line("public static void put(Context context ,%s obj,String suffixTag){\n", proxyClassName));

        for (ClassInfo classInfo : classInfos) {
            builder.append(line("put%s(context,obj.%s,suffixTag);", captureName(classInfo.getFieldName()), classInfo.getFieldName()));
        }
        builder.append("}");

        builder.append("}\n");
        return builder.toString();
    }

    private String getDefaultValue(String typeName, String value) {
        if (value == null) {
            return str2Default(typeName);
        }
        if (typeName.contains("String")) {
            return line("\"%s\"", value);
        }
        return value;
    }


    private String str2Default(String typeName) {
        {
            if (typeName.contains("int") || typeName.contains("Integer") || typeName.contains("long") || typeName.contains("Long")) {
                return "0";
            } else if (typeName.contains("float") || typeName.contains("Float") || typeName.contains("double") || typeName.contains("Double")) {
                return "0.0";
            } else if (typeName.contains("String")) {
                return "\"\"";
            } else if (typeName.contains("boolean") || typeName.contains("Boolean")) {
                return "false";
            } else {
                return null;
            }
        }
    }


    private void print(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }


    private String line(String format, Object... args) {
        return String.format(format, args);
    }
}
