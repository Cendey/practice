package edu.lab.mit.utils;

public class StringSimilarity {

    /**
     * <em>Calculates the similarity (a number within 0 and 1) between two strings.</em>
     *
     * @param one     <p>One string content</p>
     * @param another <p>Another string content</p>
     * @return <p>Cosine similarity</p>
     */
    public static double similarity(String one, String another) {
        String longer = one, shorter = another;
        if (one.length() < another.length()) { // longer should always have greater length
            longer = another;
            shorter = one;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
    /*
    If you have StringUtils, you can use it to calculate the edit distance:
    return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) / (double) longerLength;
     */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    /**
     * <em>Calculate the edit distance for two strings</em>
     *
     * @param one     <p>One string content</p>
     * @param another <p>Another string content</p>
     * @return <p>Distance</p>
     * @see <p>http://rosettacode.org/wiki/Levenshtein_distance#Java</p>
     */
    // Example implementation of the Levenshtein Edit Distance
    public static int editDistance(String one, String another) {
        one = one.toLowerCase();
        another = another.toLowerCase();

        int[] costs = new int[another.length() + 1];
        for (int i = 0; i <= one.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= another.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (one.charAt(i - 1) != another.charAt(j - 1))
                            newValue = Math.min(
                                Math.min(newValue, lastValue),
                                costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[another.length()] = lastValue;
        }
        return costs[another.length()];
    }

    public static void printSimilarity(String s, String t) {
        System.out.println(String.format(
            "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
    }

    public static void main(String[] args) {
        printSimilarity("", "");
        printSimilarity("1234567890", "1");
        printSimilarity("1234567890", "123");
        printSimilarity("1234567890", "1234567");
        printSimilarity("1234567890", "1234567890");
        printSimilarity("1234567890", "1234567980");
        printSimilarity("47/2010", "472010");
        printSimilarity("47/2010", "472011");
        printSimilarity("47/2010", "AB.CDEF");
        printSimilarity("47/2010", "4B.CDEFG");
        printSimilarity("47/2010", "AB.CDEFG");
        printSimilarity("The quick fox jumped", "The fox jumped");
        printSimilarity("The quick fox jumped", "The fox");
        printSimilarity("kitten", "sitting");
        printSimilarity("Tried to load library 'Linux_x86_EtherAddr' (filename assumed to be 'libLinux_x86_EtherAddr.so'): error; java.lang.UnsatisfiedLinkError: /data1/domains/kff_v33.3_7333/jug-native/libLinux_x86_EtherAddr.so (/data1/domains/kff_v33.3_7333/jug-native/libLinux_x86_EtherAddr.so: wrong ELF class: ELFCLASS32)\n"
            + "java.lang.Error: Tried to load library 'Linux_x86_EtherAddr' (filename assumed to be 'libLinux_x86_EtherAddr.so'): error; java.lang.UnsatisfiedLinkError: /data1/domains/kff_v33.3_7333/jug-native/libLinux_x86_EtherAddr.so (/data1/domains/kff_v33.3_7333/jug-native/libLinux_x86_EtherAddr.so: wrong ELF class: ELFCLASS32)\n"
            + "\tat org.doomdark.uuid.NativeInterfaces.loadAppLib(Unknown Source)\n"
            + "\tat org.doomdark.uuid.NativeInterfaces.checkLoad(Unknown Source)\n"
            + "\tat org.doomdark.uuid.NativeInterfaces.getAllInterfaces(Unknown Source)\n"
            + "\tat com.ipacs.als.install.LicenseMaker.getMacAddress(LicenseMaker.java:134)\n"
            + "\tat com.ipacs.als.install.LicenseMaker.generateLicenseContext(LicenseMaker.java:88)\n"
            + "\tat com.ipacs.als.install.LicenseGate.isValidLicenseInfo(LicenseGate.java:355)\n"
            + "\tat com.ipacs.als.server.helper.LicenseHelper.isValidLicenseInfo(LicenseHelper.java:74)\n"
            + "\tat com.ipacs.als.authen.LoginLicenseHelper.doCheckLicense(LoginLicenseHelper.java:63)\n"
            + "\tat com.ipacs.als.authen.AuthenLogin.getUserInfo(AuthenLogin.java:205)\n"
            + "\tat com.ipacs.als.action.Login.perform(Login.java:109)\n"
            + "\tat com.ipacs.framework.action.BusinessAction.performAction(BusinessAction.java:415)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB.executeWithTransactionFirstTime(ServerControllerEJB.java:283)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB.excuteFirstTime(ServerControllerEJB.java:376)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB.execute(ServerControllerEJB.java:171)\n"
            + "\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n"
            + "\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)\n"
            + "\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:37)\n"
            + "\tat java.lang.reflect.Method.invoke(Method.java:599)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:310)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:182)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:149)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.doProceed(DelegatingIntroductionInterceptor.java:131)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.invoke(DelegatingIntroductionInterceptor.java:119)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)\n"
            + "\tat com.bea.core.repackaged.springframework.jee.spi.MethodInvocationVisitorImpl.visit(MethodInvocationVisitorImpl.java:37)\n"
            + "\tat weblogic.ejb.container.injection.EnvironmentInterceptorCallbackImpl.callback(EnvironmentInterceptorCallbackImpl.java:54)\n"
            + "\tat com.bea.core.repackaged.springframework.jee.spi.EnvironmentInterceptor.invoke(EnvironmentInterceptor.java:50)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:89)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.doProceed(DelegatingIntroductionInterceptor.java:131)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.invoke(DelegatingIntroductionInterceptor.java:119)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:204)\n"
            + "\tat $Proxy87.execute(Unknown Source)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB_f1r1ds_IServerControllerImpl.execute(ServerControllerEJB_f1r1ds_IServerControllerImpl.java:61)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB_f1r1ds_IServerControllerImpl_WLSkel.invoke(Unknown Source)\n"
            + "\tat weblogic.rmi.internal.BasicServerRef.invoke(BasicServerRef.java:589)\n"
            + "\tat weblogic.rmi.cluster.ClusterableServerRef.invoke(ClusterableServerRef.java:230)\n"
            + "\tat weblogic.rmi.internal.BasicServerRef$1.run(BasicServerRef.java:477)\n"
            + "\tat weblogic.security.acl.internal.AuthenticatedSubject.doAs(AuthenticatedSubject.java:363)\n"
            + "\tat weblogic.security.service.SecurityManager.runAs(SecurityManager.java:147)\n"
            + "\tat weblogic.rmi.internal.BasicServerRef.handleRequest(BasicServerRef.java:473)\n"
            + "\tat weblogic.rmi.internal.wls.WLSExecuteRequest.run(WLSExecuteRequest.java:118)\n"
            + "\tat weblogic.work.ExecuteThread.execute(ExecuteThread.java:201)\n"
            + "\tat weblogic.work.ExecuteThread.run(ExecuteThread.java:173)", "Tried to load library 'Linux_x86_EtherAddr' (filename assumed to be 'libLinux_x86_EtherAddr.so'): error; java.lang.UnsatisfiedLinkError: /data1/domains/kff_v33.3_7333/jug-native/libLinux_x86_EtherAddr.so (/data1/domains/kff_v33.3_7333/jug-native/libLinux_x86_EtherAddr.so: wrong ELF class: ELFCLASS32)\n"
            + "java.lang.Error: Tried to load library 'Linux_x86_EtherAddr' (filename assumed to be 'libLinux_x86_EtherAddr.so'): error; java.lang.UnsatisfiedLinkError: /data1/domains/kff_v33.3_7333/jug-native/libLinux_x86_EtherAddr.so (/data1/domains/kff_v33.3_7333/jug-native/libLinux_x86_EtherAddr.so: wrong ELF class: ELFCLASS32)\n"
            + "\tat org.doomdark.uuid.NativeInterfaces.loadAppLib(Unknown Source)\n"
            + "\tat org.doomdark.uuid.NativeInterfaces.checkLoad(Unknown Source)\n"
            + "\tat org.doomdark.uuid.NativeInterfaces.getAllInterfaces(Unknown Source)\n"
            + "\tat com.ipacs.als.install.LicenseMaker.getMacAddress(LicenseMaker.java:134)\n"
            + "\tat com.ipacs.als.install.LicenseMaker.generateLicenseContext(LicenseMaker.java:88)\n"
            + "\tat com.ipacs.als.install.LicenseGate.isValidLicenseInfo(LicenseGate.java:355)\n"
            + "\tat com.ipacs.als.server.helper.LicenseHelper.isValidLicenseInfo(LicenseHelper.java:74)\n"
            + "\tat com.ipacs.als.authen.LoginLicenseHelper.doCheckLicense(LoginLicenseHelper.java:63)\n"
            + "\tat com.ipacs.als.authen.AuthenLogin.getUserInfo(AuthenLogin.java:205)\n"
            + "\tat com.ipacs.als.action.Login.perform(Login.java:109)\n"
            + "\tat com.ipacs.framework.action.BusinessAction.performAction(BusinessAction.java:415)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB.executeWithTransactionFirstTime(ServerControllerEJB.java:283)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB.excuteFirstTime(ServerControllerEJB.java:376)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB.execute(ServerControllerEJB.java:171)\n"
            + "\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n"
            + "\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)\n"
            + "\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:37)\n"
            + "\tat java.lang.reflect.Method.invoke(Method.java:599)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:310)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:182)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:149)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.doProceed(DelegatingIntroductionInterceptor.java:131)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.invoke(DelegatingIntroductionInterceptor.java:119)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)\n"
            + "\tat com.bea.core.repackaged.springframework.jee.spi.MethodInvocationVisitorImpl.visit(MethodInvocationVisitorImpl.java:37)\n"
            + "\tat weblogic.ejb.container.injection.EnvironmentInterceptorCallbackImpl.callback(EnvironmentInterceptorCallbackImpl.java:54)\n"
            + "\tat com.bea.core.repackaged.springframework.jee.spi.EnvironmentInterceptor.invoke(EnvironmentInterceptor.java:50)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:89)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.doProceed(DelegatingIntroductionInterceptor.java:131)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.invoke(DelegatingIntroductionInterceptor.java:119)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)\n"
            + "\tat com.bea.core.repackaged.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:204)\n"
            + "\tat $Proxy80.execute(Unknown Source)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB_f1r1ds_IServerControllerImpl.execute(ServerControllerEJB_f1r1ds_IServerControllerImpl.java:61)\n"
            + "\tat com.ipacs.framework.controller.ejb.ServerControllerEJB_f1r1ds_IServerControllerImpl_WLSkel.invoke(Unknown Source)\n"
            + "\tat weblogic.rmi.internal.BasicServerRef.invoke(BasicServerRef.java:589)\n"
            + "\tat weblogic.rmi.cluster.ClusterableServerRef.invoke(ClusterableServerRef.java:230)\n"
            + "\tat weblogic.rmi.internal.BasicServerRef$1.run(BasicServerRef.java:477)\n"
            + "\tat weblogic.security.acl.internal.AuthenticatedSubject.doAs(AuthenticatedSubject.java:363)\n"
            + "\tat weblogic.security.service.SecurityManager.runAs(SecurityManager.java:147)\n"
            + "\tat weblogic.rmi.internal.BasicServerRef.handleRequest(BasicServerRef.java:473)\n"
            + "\tat weblogic.rmi.internal.wls.WLSExecuteRequest.run(WLSExecuteRequest.java:118)\n"
            + "\tat weblogic.work.ExecuteThread.execute(ExecuteThread.java:201)\n"
            + "\tat weblogic.work.ExecuteThread.run(ExecuteThread.java:173)");
    }

}
