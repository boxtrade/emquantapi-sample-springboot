package com.eastmoney.emquantapisample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class QuantCaseStart implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(QuantCaseStart.class);

    @Value("${emquantapi.libpath.userAPI}")
    private String userAPIPath;

    @Value("${emquantapi.libpath.jniEnv}")
    private String jniEnvPath;

    @Value("${emquantapi.libpath.serverlistdir}")
    private String serverlistdir;

    @Value("${emquantapi.manualactivate.run}")
    private boolean manualactivateRun;

    @Value("${emquantapi.manualactivate.userName}")
    private String manualactivateUserName;

    @Value("${emquantapi.manualactivate.password}")
    private String manualactivatePassword;

    @Value("${emquantapi.manualactivate.options}")
    private String manualactivateOptions;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("QuantCaseStart...");

        /*
         userAPIPath，jniEnvPath，serverlistdir 为量化api库自定义路径，如果不需要自定义，置为null
         如果在容器中运行，路径为容器中路径，且需要emquantapi_library copy一份到容器中
         */

        QuantCase quantCase = new QuantCase();
        quantCase.init(userAPIPath,jniEnvPath);

        //application.yml：emquantapi.manualactivate.run 控制是否进行手动激活
        //首次或者激活时需要打开，正常运行时需置为：false
        if (manualactivateRun) {
            logger.info("Manualactivate...");
            quantCase.testManualactivate(manualactivateUserName, manualactivatePassword, manualactivateOptions);
            logger.info("Manualactivate end!");
            return;
        }

        //调用登录函数
        quantCase.start(serverlistdir);

        //资讯查询
        quantCase.testCfn();
        //资讯订阅
        quantCase.testCnq();
        //cmc使用
        quantCase.testCmc();
        //csd使用
        quantCase.testCsd();
        //css使用
        quantCase.testCss();
        //cses
        quantCase.testCses();
        //sector使用
        quantCase.testSector();
        //tradedate使用
        quantCase.testTradedates();
        //getdate使用
        quantCase.testGetDate();
        //实时行情订阅使用
        quantCase.testCsq();
        //日内跳价使用
        quantCase.testCst();
        //行情快照使用
        quantCase.testCsqsnapshot();
        //获取专题报表使用
        quantCase.testCtr();
        //证券与指标校验函数
        quantCase.testCfc();
        //校验或补全东财证券代码函数
        quantCase.testCec();
        //选股使用
        quantCase.testCps();
        //宏观指标服务
        quantCase.testEdb();
        //组合使用
        quantCase.testPgroup();
        //获取区间日期内的交易日天数
        quantCase.testTradedatesNum();
        //退出
        quantCase.stop();

        logger.info("QuantCaseStart end!");

    }
}
