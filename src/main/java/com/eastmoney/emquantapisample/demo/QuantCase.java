package com.eastmoney.emquantapisample.demo;

import com.eastmoney.api.UserAPI;
import com.eastmoney.callback.user.UserCallBack;
import com.eastmoney.callback.user.UserDataCallBack;
import com.eastmoney.callback.user.UserLogCallBack;
import com.eastmoney.constant.ErrorType;
import com.eastmoney.entity.EQCtrData;
import com.eastmoney.entity.EQData;
import com.eastmoney.entity.EQMSG;
import com.eastmoney.entity.ReturnSignal;
import com.eastmoney.thread.ExplainClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author: ThinkingLee
 * @date: 2020/11/4
 * @desc: 量化测试样例
 */
public class QuantCase {

	private static Logger LOG = LoggerFactory.getLogger(QuantCase.class);
	private static UserAPI userAPI;

	public void init() {
		HashMap<String, UserCallBack> userCallBackHashMap = new HashMap<String, UserCallBack>();
		userCallBackHashMap.put("msg", new UserDataCallBack() {
			@Override
			public void callback(EQMSG eqmsg) {
				if (eqmsg.getRequestID() == 10000) {
					LOG.info("csq callback:" + eqmsg.toString());
					// eqmsg.getSerialID()与csq函数返回对象ReturnSignal中的returnSignal.getDataOreqid()相对应
					if (eqmsg.getSerialID() == 1) {
						// TODO
					}
				} else if (eqmsg.getRequestID() == 10001) {
					LOG.info("cst callback:" + eqmsg.toString());
				} else if (eqmsg.getRequestID() == 10002) {
					LOG.info("cnq callback:" + eqmsg.toString());
				} else if (eqmsg.getRequestID() == 10003) {
					LOG.info("chq callback:" + eqmsg.toString());
				}
			}
		});
		userCallBackHashMap.put("log", new UserLogCallBack() {
			@Override
			public void callback(String o) {
				LOG.info(o);
			}
		});
		ExplainClient.start(userCallBackHashMap);
		// 方案一:EmQuantAPIJavaWraper/EmQuantAPIJavaWraper_x64与jar包在同级目录(默认方案)
		userAPI = new UserAPI(ExplainClient.getSystemTaskQueue());
		// 方案二:根据实际路径设置EmQuantAPIJavaWraper/EmQuantAPIJavaWraper_x64所在目录(自定义方案)
		// userAPI = new
		// UserAPI("D:/EmQuantAPI_Java/EmQuantAPITest/EmQuantAPITest/Wraper/",ExplainClient.getSystemTaskQueue());
		try {
			// 方案一:EmQuantAPI/EmQuantAPI_x64与jar包在同级目录(默认方案)
			userAPI.setJniEnv("");
			// 方案二:根据实际路径设置EmQuantAPI/EmQuantAPI_x64所在目录(自定义方案)
			// userAPI.setJniEnv("D:/EmQuantAPI_Java/EmQuantAPITest/EmQuantAPITest/dll/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start(boolean isStartOptionLogin) {
		// 方案一:激活文件与jar包在同级目录(默认方案)
		userAPI.setserverlistdir("");
		// 方案二:根据实际路径设置激活文件所在目录(自定义方案)
		// userAPI.setserverlistdir("D:/EmQuantAPI_Java/EmQuantAPITest/EmQuantAPITest/login/");

		String option = "";
		if (isStartOptionLogin) {
			// 账号密码通过start函数的option参数登录
			Scanner sc = new Scanner(System.in);
			LOG.info("请输入API账户用户名:");
			String userName = sc.next();
			LOG.info("请输入API账户密码:");
			String passWord = sc.next();
			option = "UserName=" + userName + ",Password=" + passWord;
		}

		int result = userAPI.start(option);
		if (result != 0) {
			LOG.info(ErrorType.getErrorString(result));
		}
	}

	// 资讯查询使用范例
	public void testCfn() {
		EQData cfn = userAPI.cfn("300059.SZ,600519.SH,300024.SZ",
				"companynews,industrynews", 2,
				" starttime=20190501010000,endtime=20190725200000,count=10");

		LOG.info("cfn输出结果======分隔线======");
		if (cfn.getErrCode() != 0) {
			LOG.info("request cfn Error, {}", cfn.getErrString());
		} else {
			LOG.info(cfn.toString());
		}

		// 板块树查询使用范例
		EQData cfnquery = userAPI.cfnquery("");
		LOG.info("cfnquery输出结果======分隔线======");
		if (cfnquery.getErrCode() != 0) {
			LOG.info("request cfnquery Error, {}", cfnquery.getErrString());
		} else {
			LOG.info(cfnquery.toString());
		}
	}

	// 资讯订阅使用范例
	public void testCnq() {
		ReturnSignal cnq = userAPI.cnq("S888005002API", "sectornews", "");
		if (cnq.getErrId() != 0) {
			LOG.info("request cnq Error, " + cnq.getErrString());
		} else {
			try {
				Thread.sleep(30000);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			int cnqcancel = userAPI.cnqcancel(0);
			LOG.info("cnqcancel result:" + ErrorType.getErrorString(cnqcancel));
		}
	}

	// cmc使用范例
	public void testCmc() {
		String code = "300059.SZ";
		String indicators = "DATE,TIME,HIGH,OPEN,LOW,CLOSE";
		String startDate = getDate(-7);
		String endDate = getDate(0);
		String options = "";

		EQData cmc = userAPI.cmc(code, indicators, startDate, endDate, options);
		LOG.info("cmc输出结果======分隔线======");
		if (cmc.getErrCode() != 0) {
			LOG.info("request Cmc Error, " + cmc.getErrString());
		} else {
			LOG.info(cmc.toString());
		}
	}

	// cmc todataframe使用范例
	public void testCmcToDataFrame() {
		String code = "300059.SZ";
		String indicators = "DATE,TIME,HIGH,OPEN,LOW,CLOSE";
		String startDate = getDate(-7);
		String endDate = getDate(0);
		String options = "";

		EQData cmc = userAPI.cmc(code, indicators, startDate, endDate, options,
				1, 1);
		LOG.info("cmc输出结果======分隔线======");
		if (cmc != null) {
			if (cmc.getErrCode() != 0) {
				LOG.info("request Cmc Error, " + cmc.getErrString());
			} else {
				LOG.info(cmc.toString());
			}
		}
	}

	// csd使用范例
	public void testCsd() {

		String codes = "000002.SZ,300059.SZ";
		String indicator = "HIGH,OPEN,CLOSE";
		String startDate = "2016/01/10";
		String endDate = "2016/04/13";

		EQData csd = userAPI.csd(codes, indicator, startDate, endDate, "");
		LOG.info("csd输出结果======分隔线======");
		if (csd.getErrCode() != 0) {
			LOG.info("request csd Error, " + csd.getErrString());
		} else {
			LOG.info(csd.toString());
		}
	}

	// csd todataframe使用范例
	public void testCsdToDataFrame() {

		String codes = "000002.SZ,300059.SZ";
		String indicator = "HIGH,OPEN,CLOSE";
		String startDate = "2016/01/10";
		String endDate = "2016/04/13";

		EQData csd = userAPI
				.csd(codes, indicator, startDate, endDate, "", 1, 1);
		LOG.info("csd输出结果======分隔线======");
		if (csd != null) {
			if (csd.getErrCode() != 0) {
				LOG.info("request csd Error, " + csd.getErrString());
			} else {
				LOG.info(csd.toString());
			}
		}
	}

	// css使用范例
	public void testCss() {
		String codes = "000002.SZ,300059.SZ";
		String indicator = "OPEN,CLOSE,HIGH";
		String options = " AdjustFlag=1,TradeDate=20160217";

		EQData css = userAPI.css(codes, indicator, options);
		LOG.info("css输出结果======分隔线======");
		if (css.getErrCode() != 0) {
			LOG.info("request css Error, " + css.getErrString());
		} else {
			System.out.println(css);
		}
	}

	// css todataframe使用范例
	public void testCssToDataFrame() {
		String codes = "000002.SZ,300059.SZ";
		String indicator = "OPEN,CLOSE,HIGH";
		String options = " AdjustFlag=1,TradeDate=20160217";

		EQData css = userAPI.css(codes, indicator, options, 1, 1);
		LOG.info("css输出结果======分隔线======");
		if (css != null) {
			if (css.getErrCode() != 0) {
				LOG.info("request css Error, " + css.getErrString());
			} else {
				System.out.println(css);
			}
		}
	}

	// Cses使用范例
	public void testCses() {
		String blockcodes = "B_011001003,B_018005001001,B_007159,B_014010016006002,B_003002001005,B_027004002003001";
		String indicator = "SECTOPREAVG,CFOPSAVG,MANAEXPAVG,SECTORCOUNT";
		String options = "TradeDate=2020-10-19,DelType=1,IsHistory=0,type=1,ReportDate=2020-06-30,DataAdjustType=1,PREDICTYEAR=2020,StartDate=2019-05-30,EndDate=2020-10-19,Payyear=2019";

		EQData cses = userAPI.cses(blockcodes, indicator, options);
		if (cses.getErrCode() != 0) {
			LOG.info("request cses Error, "
					+ ErrorType.getErrorString(cses.getErrCode()));
		} else {
			LOG.info(cses.toString());
		}
	}

	// Cses todataframe使用范例
	public void testCsesToDataFrame() {
		String blockcodes = "B_011001003,B_018005001001,B_007159,B_014010016006002,B_003002001005,B_027004002003001";
		String indicator = "SECTOPREAVG,CFOPSAVG,MANAEXPAVG,SECTORCOUNT";
		String options = "TradeDate=2020-10-19,DelType=1,IsHistory=0,type=1,ReportDate=2020-06-30,DataAdjustType=1,PREDICTYEAR=2020,StartDate=2019-05-30,EndDate=2020-10-19,Payyear=2019";

		EQData cses = userAPI.cses(blockcodes, indicator, options, 1, 1);
		if (cses != null) {
			if (cses.getErrCode() != 0) {
				LOG.info("request cses Error, "
						+ ErrorType.getErrorString(cses.getErrCode()));
			} else {
				LOG.info(cses.toString());
			}
		}
	}

	// sector使用范例
	// 001004 全部A股板块
	public void testSector() {

		String pukeycode = "001004";
		String endDate = "2016-04-26";
		String options = "";

		EQData sector = userAPI.sector(pukeycode, endDate, options);
		LOG.info("sector输出结果======分隔线======");
		if (sector.getErrCode() != 0) {
			LOG.info("request Sector Error, " + sector.getErrString());
		} else {
			LOG.info(sector.toString());
		}

	}

	// sector todataframe使用范例
	public void testSectorToDataFrame() {

		String pukeycode = "001004";
		String endDate = "2016-04-26";
		String options = "";

		EQData sector = userAPI.sector(pukeycode, endDate, options, 1);
		LOG.info("sector输出结果======分隔线======");
		if (sector != null) {
			if (sector.getErrCode() != 0) {
				LOG.info("request Sector Error, " + sector.getErrString());
			} else {
				LOG.info(sector.toString());
			}
		}

	}

	// getdate使用范例
	public void testGetDate() {
		String tradedate = "20160426";
		int offday = -3;
		String options = "Market=CNSESH";

		EQData eqData = userAPI.getdate(tradedate, offday, options);
		if (eqData.getErrCode() != 0) {
			LOG.info("request GetDate Error, "
					+ ErrorType.getErrorString(eqData.getErrCode()));
		} else {
			LOG.info(eqData.toString());
		}
	}

	// 实时行情订阅使用范例
	public void testCsq() {
		ReturnSignal csqnum = userAPI.csq("600000.SH,300059.SZ",
				"TIME,Now,Volume", "Pushtype=0");

		LOG.info("csq输出结果======分隔线======");
		if (csqnum.getErrId() != 0) {
			LOG.info("request csq Error, "
					+ ErrorType.getErrorString(csqnum.getErrId()));
		} else {
			try {
				Thread.sleep(30000);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			// 取消订阅
			int csqcancel = userAPI.csqcancel(csqnum.getDataOreqid());
			LOG.info("csqcancel result:" + ErrorType.getErrorString(csqcancel));
		}
	}

	// 日内跳价使用范例
	public void testCst() {
		ReturnSignal cst = userAPI.cst("600000.SH,300059.SZ",
				"TIME,OPEN,HIGH,LOW,NOW", "093000", "094000", "");

		LOG.info("cst输出结果======分隔线======");
		if (cst.getErrId() != 0) {
			LOG.info("request cst Error, "
					+ ErrorType.getErrorString(cst.getErrId()));
		} else {
			LOG.info(cst.toString());
		}

	}

	// 行情快照使用范例
	public void testCsqsnapshot() {

		String codes = "000005.SZ,600602.SH,600652.SH,600653.SH,600654.SH,600601.SH,600651.SH,000004.SZ,000002.SZ,000001.SZ,000009.SZ";
		String indicators = "PRECLOSE,OPEN,HIGH,LOW,NOW,AMOUNT";
		String options = "";

		EQData eqData = userAPI.csqsnapshot(codes, indicators, options);
		LOG.info("csqsnapshot输出结果======分隔线======");
		if (eqData.getErrCode() != 0) {
			LOG.info("request csqsnapshot Error, "
					+ ErrorType.getErrorString(eqData.getErrCode()));
		} else {
			LOG.info(eqData.toString());
		}
	}

	// 行情快照todataframe使用范例
	public void testCsqsnapshotToDataFrame() {

		String codes = "000005.SZ,600602.SH,600652.SH,600653.SH,600654.SH,600601.SH,600651.SH,000004.SZ,000002.SZ,000001.SZ,000009.SZ";
		String indicators = "PRECLOSE,OPEN,HIGH,LOW,NOW,AMOUNT";
		String options = "";

		EQData eqData = userAPI.csqsnapshot(codes, indicators, options, 1, 1);
		if (eqData != null) {
			if (eqData.getErrCode() != 0) {
				LOG.info("request csqsnapshot Error, "
						+ ErrorType.getErrorString(eqData.getErrCode()));
			} else {
				LOG.info(eqData.toString());
			}
		}
	}

	// 获取专题报表使用范例
	public void testCtr() {

		String ctrName = "StockInfo";
		String indicators = "";
		String options = "StartDate=2019-07-01,EndDate=2019-07-02";

		EQCtrData eqCtrData = userAPI.ctr(ctrName, indicators, options);
		LOG.info("ctr输出结果======分隔线======");
		if (eqCtrData.getErrCode() != 0) {
			LOG.info("request ctr Error, "
					+ ErrorType.getErrorString(eqCtrData.getErrCode()));
		} else {
			LOG.info(eqCtrData.toString());
		}
	}

	// 获取专题报表 todataframe使用范例
	public void testCtrToDataFrame() {

		String ctrName = "StockInfo";
		String indicators = "";
		String options = "StartDate=2019-07-01,EndDate=2019-07-02";

		EQCtrData eqCtrData = userAPI.ctr(ctrName, indicators, options, 1);
		LOG.info("ctr输出结果======分隔线======");
		if (eqCtrData != null) {
			if (eqCtrData.getErrCode() != 0) {
				LOG.info("request ctr Error, "
						+ ErrorType.getErrorString(eqCtrData.getErrCode()));
			} else {
				LOG.info(eqCtrData.toString());
			}
		}

	}

	// 证券与指标校验函数
	public void testCfc() {
		EQCtrData cfc = userAPI.cfc(
				"000001.SZ,000001.SH,600000.SH,000000.TEST", "CODE,NAME,TEST",
				"FunType=css");
		if (cfc.getErrCode() != 0) {
			LOG.info("request cfc Error, " + cfc.getErrString());
		} else {
			LOG.info(cfc.toString());
		}
	}

	// 校验或补全东财证券代码函数
	public void testCec() {
		EQCtrData cec = userAPI.cec(
				"000001.SZ,000001.SH,600000.SH,000000.TEST", "ReturnType=0");
		if (cec.getErrCode() != 0) {
			LOG.info("request cec Error, " + cec.getErrString());
		} else {
			LOG.info(cec.toString());
		}
	}

	// 选股使用范例
	public void testCps() {

		EQData cps = userAPI.cps("B_001004", "s0,OPEN,2017/2/27,1;s1,NAME",
				"[s0]>0", "orderby=rd([s0]),top=max([s0],100)");
		LOG.info("cps输出结果======分隔线======");
		if (cps.getErrCode() != 0) {
			LOG.info("request cps Error, "
					+ ErrorType.getErrorString(cps.getErrCode()));
		} else {
			LOG.info(cps.toString());
		}
	}

	// 宏观指标服务
	public void testEdb() {

		EQData edb = userAPI.edb("EMM00087117", "IsPublishDate=1");
		LOG.info("edb输出结果======分隔线======");
		if (edb.getErrCode() != 0) {
			LOG.info("request edb Error, "
					+ ErrorType.getErrorString(edb.getErrCode()));
		} else {
			LOG.info(edb.toString());
		}

		EQData edbquery = userAPI.edbquery(
				"EMM00058124,EMM00087117,EMG00147350", "", "");
		LOG.info("edbquery输出结果======分隔线======");
		if (edbquery.getErrCode() != 0) {
			LOG.info("request edbquery Error, "
					+ ErrorType.getErrorString(edbquery.getErrCode()));
		} else {
			LOG.info(edbquery.toString());
		}
	}

	// 宏观指标服务todataframe使用范例
	public void testEdbToDataFrame() {

		EQData edb = userAPI.edb("EMM00087117", "IsPublishDate=1", 1, 1);
		LOG.info("edb输出结果======分隔线======");
		if (edb != null) {
			if (edb.getErrCode() != 0) {
				LOG.info("request edb Error, "
						+ ErrorType.getErrorString(edb.getErrCode()));
			} else {
				LOG.info(edb.toString());
			}
		}
	}

	// 组合操作
	public void testPgroup() {

		// 新建组合
		int pcreate = userAPI.pcreate("quant001.PF", "组合牛股", 1000000,
				"这是一个牛股的组合", "");
		if (pcreate != 0) {
			LOG.info("request pcreate Error, "
					+ ErrorType.getErrorString(pcreate));
		} else {
			LOG.info("pcreate success");
		}

		// 组合资金调配
		int pctransfer = userAPI.pctransfer("quant001.PF", "IN", "2019-08-13",
				100000, "追加资金", "TRANSFERTYPE=1");
		if (pctransfer != 0) {
			LOG.info("request pctransfer Error, "
					+ ErrorType.getErrorString(pctransfer));
		} else {
			LOG.info("pctransfer success");
		}

		// 组合下单
		int porder = userAPI
				.porder("[{\"code\":\"002024.SZ\",\"volume\":100.0,\"price\":2,\"date\":20170725,\"time\":131815,\"optype\":1,\"cost\":0.0,\"rate\":0.0,\"reserve\":0}]",
						"quant001.PF", "我是下单测试", "");
		if (porder != 0) {
			LOG.info("request porder Error, "
					+ ErrorType.getErrorString(porder));
		} else {
			LOG.info("porder success");
		}

		// 组合报表查询
		EQData preport = userAPI.preport("quant001.PF", "record",
				"startdate=2017/07/12,enddate=2018/01/15");
		if (preport.getErrCode() != 0) {
			LOG.info("request preport Error, "
					+ ErrorType.getErrorString(preport.getErrCode()));
		} else {
			LOG.info("preport success");
		}

		// 组合信息查询
		EQData query = userAPI.pquery("");
		if (query.getErrCode() != 0) {
			LOG.info("request pquery Error, "
					+ ErrorType.getErrorString(query.getErrCode()));
		} else {
			LOG.info("pquery success");
		}

		// 删除组合
		int pdelete = userAPI.pdelete("quant001.PF", "");
		if (pdelete != 0) {
			LOG.info("request pdelete Error, "
					+ ErrorType.getErrorString(pdelete));
		} else {
			LOG.info("pdelete success");
		}
	}

	public void stop() {
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		userAPI.stop();
	}

	// 获取错误码文本说明
	public void testGeterrstring() {

		int errcode = 10001012;
		int lang = 0;// 语言枚举

		String geterrstring = userAPI.geterrstring(errcode, lang);
		System.out.println(geterrstring);

	}

	// 设置网络代理 注：如需使用代理，需要在调用所有接口之前设置
	public void testSetproxy() {
		int type = 0;
		String ip = "120.0.0.0";
		int port = 8080;
		boolean verify = true;
		String user = "choice";
		String pwd = "password";

		int result = userAPI.setproxy(type, ip, port, verify, user, pwd);
		System.out.println(ErrorType.getErrorString(result));
	}

	// 人工激活
	public void testManualactivate() {

		String usr = "usr";
		String pwd = "pwd";
		String options = "email=xxx@163.com";

		int result = userAPI.manualactivate(usr, pwd, options);
		System.out.println(ErrorType.getErrorString(result));
	}

	// 获取区间日期内的交易日(同步请求)
	public void testTradedates() {

		String startDate = "2016-07-01";
		String endDate = "2016-07-12";
		String options = "Period=1,Order=1,Market=CNSESH";

		EQData tradedates = userAPI.tradedates(startDate, endDate, options);
		LOG.info("tradedate输出结果======分隔线======");
		if (tradedates.getErrCode() != 0) {
			LOG.info("request tradedates Error, "
					+ ErrorType.getErrorString(tradedates.getErrCode()));
		} else {
			LOG.info(tradedates.toString());
		}

	}

	// 获取区间日期内的交易日天数(同步请求)
	public void testTradedatesNum() {

		String startDate = "2016-3-14";
		String endDate = "2016-6-1";
		String options = "";

		ReturnSignal tradedatesnum = userAPI.tradedatesnum(startDate, endDate,
				options);
		if (tradedatesnum.getErrId() != 0) {
			LOG.info("request TradedatesNum Error, "
					+ ErrorType.getErrorString(tradedatesnum.getErrId()));
		} else {
			LOG.info(tradedatesnum.toString());
		}
	}

	private String getDate(int offDay) {

		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, offDay);
		return sf.format(c.getTime());

	}

}
