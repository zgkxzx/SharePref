package com.fcbox.lib.wxpay;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @Created zgkxzx
 * @Date 2018/5/19.
 * @Description
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class WxProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }


    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(ApplicationName.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(ApplicationName.class);
        if (annotatedElements == null || annotatedElements.size() < 1) {
            return false;
        }
        String packageName = null;
        for (Element element : annotatedElements) {
            packageName = element.getAnnotation(ApplicationName.class).value();
        }

        ClassName baseReqClass = ClassName.bestGuess("com.tencent.mm.opensdk.modelbase.BaseReq");
        ClassName baseRespClass = ClassName.bestGuess("com.tencent.mm.opensdk.modelbase.BaseResp");
        ClassName InterfaceName = ClassName.get("com.tencent.mm.opensdk.openapi", "IWXAPIEventHandler");
        ClassName activityClass = ClassName.bestGuess("android.app.Activity");//
        ClassName bundleClass = ClassName.bestGuess("android.os.Bundle");
        ClassName iwxAPiClass = ClassName.bestGuess("com.tencent.mm.opensdk.openapi.IWXAPI");
        ClassName intentClass = ClassName.bestGuess("android.content.Intent");

        MethodSpec.Builder onCreateMethodSpec = MethodSpec.methodBuilder("onCreate")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(bundleClass, "savedInstanceState")
                .addStatement("  super.onCreate(savedInstanceState);\n" +
                        "\t\tString weiXinAppId = com.fcbox.lib.pay.utils.XmlUtil.getWeiXinAppIdByXML(this);\n" +
                        "        api = com.tencent.mm.opensdk.openapi.WXAPIFactory.createWXAPI(this, weiXinAppId);\n" +
                        "        api.handleIntent(getIntent(), this)");

        MethodSpec.Builder onNewIntentMethodSpec = MethodSpec.methodBuilder("onNewIntent")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(intentClass, "intent")
                .addStatement(" super.onNewIntent(intent);\n" +
                        "        setIntent(intent);\n" +
                        "        api.handleIntent(intent, this)");

        MethodSpec.Builder onReqMethodSpec = MethodSpec.methodBuilder("onReq")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(baseReqClass, "req");

        MethodSpec.Builder onRespMethodSpec = MethodSpec.methodBuilder("onResp")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(baseRespClass, "resp")
                .addStatement(" if (resp.getType() == com.tencent.mm.opensdk.constants.ConstantsAPI.COMMAND_PAY_BY_WX) {\n" +
                        "            int code = resp.errCode;\n" +
                        "            com.fcbox.lib.pay.utils.PayBus.get().post(com.fcbox.lib.pay.platform.wx.extra.WxPayConstant.TAG_WX_PAY_RESULT, code);\n" +
                        "            finish();\n" +
                        "        }");

        TypeSpec typeBuilder = TypeSpec.classBuilder("WXPayEntryActivity")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(InterfaceName)
                .addField(iwxAPiClass, "api", Modifier.PRIVATE)
                .addMethod(onCreateMethodSpec.build())
                .addMethod(onNewIntentMethodSpec.build())
                .addMethod(onReqMethodSpec.build())
                .addMethod(onRespMethodSpec.build())
                .superclass(activityClass)
                .build();

        packageName = packageName + ".wxapi";

        StringBuilder builder = new StringBuilder();
        builder.append(" Generated code from FcPay. Do not modify!\n");
        builder.append("\n")
                .append(" @email: zgkxzx@163.com\n")
                .append(" @author: zhaoXiang\n")
                .append(line(" @date: %s\n", FormatUtil.date2StrFormat(new Date(), FormatUtil.FORMAT_DATE_TIME_02)))
                .append(line(" @description: FcPay for %s \n", packageName))
                .append("\n");

        JavaFile javaFile = JavaFile.builder(packageName, typeBuilder)
                .addFileComment(builder.toString())
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String line(String format, Object... args) {
        return String.format(format, args);
    }
}

