package com.eastmoney.emquantapisample.demo;

/**
 * @author: ThinkingLee
 * @date: 2020/11/11
 * @desc: 测试用例启动类（参考PythonDemo）
 */
public class Demo {

    public static void main(String[] args) {

        QuantCase quantCase = new QuantCase();
        quantCase.init();

        //是否通过start函数的option参数方式登录登录
        boolean isStartOptionLogin = true;
        //调用登录函数
        quantCase.start(isStartOptionLogin);
        //资讯查询
        quantCase.testCfn();
        //资讯订阅
        quantCase.testCnq();
        //cmc使用
        quantCase.testCmc();
        //cmc todataframe使用
        quantCase.testCmcToDataFrame();
        //csd使用
        quantCase.testCsd();
        //csd todataframe使用
        quantCase.testCsdToDataFrame();
        //css使用
        quantCase.testCss();
        //css todataframe使用
        quantCase.testCssToDataFrame();
        //cses
        quantCase.testCses();
        //cses todataframe使用
        quantCase.testCsesToDataFrame();
        //sector使用
        quantCase.testSector();
        //sector todataframe使用
        quantCase.testSectorToDataFrame();
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
        //行情快照 todataframe使用
        quantCase.testCsqsnapshotToDataFrame();
        //获取专题报表使用
        quantCase.testCtr();
        //获取专题报表 todataframe使用
        quantCase.testCtrToDataFrame();
        //证券与指标校验函数
        quantCase.testCfc();
        //校验或补全东财证券代码函数
        quantCase.testCec();
        //选股使用
        quantCase.testCps();
        //宏观指标服务
        quantCase.testEdb();
        //宏观指标服务 todataframe使用
        quantCase.testEdbToDataFrame();
        //组合使用
        quantCase.testPgroup();
        //获取区间日期内的交易日天数
        quantCase.testTradedatesNum();
        //退出
        quantCase.stop();
    }
}
