package com.kingteller.client.config;

/**
 * 通用URL配置
 * 
 * @author Administrator
 * 
 */
public final class URLConfig {
	public final static String DefalutIp = "218.107.9.108";
	public final static String DefaultPort = "8086";
	public final static String contextPath = "/ktcs";

	// 登陆
	public final static String LoginUrl = "/kt/mobile/mobileLogin_mobileAction.action";
	// 获取代办
	public final static String WaitDoUrl = "/kt/mobile/queryToDoList_mobileAction.action";
	// 获取菜单权限
	public final static String GetRightUrl = "/kt/mobile/queryMenu_mobileAction.action";
	// 通用数据接口
	public final static String GetCommonDataListUrl = "/kt/mobile/queryListData_mobileAction.action";
	// 公告详细
	public final static String CommonDataWebUrl = "/sysmgr/notice/queryNoticeByNoticeId_notice.action?noticeId=%1$s&userId=%2$s";
	// 技能考核
	public final static String WebJnkhUrl = "/custmgr/skillgeade/login_weiHuSkillGradeMobile.action?uid=%1$s&pwd=%2$s";
	// 获取工单列表
	public final static String WebRwdUrl = "/workreportmobile/queryWorkOrder_workordermobile.action";
	// 工程师更改工单状态
	///kt/mobile/mobileLogin_mobileAction.action
	public final static String WebRwdztUrl = "/workreportmobile/updateOrderStatus_workordermobile.action";
	//填写报告列表
	public final static String WebTxbgUrl = "/sysmgr/workreportmobile/queryWorkReportByList_workReportMobileNew.action";
	public final static String WebQtbgUrl = "/sysmgr/workreportmobile/toEdit_workReportMobileNew.action";
	public final static String otherMatterUrl = "/sysmgr/workreportmobile/doSaveOther_workReportMobileNew.action";
	// 知识库
	public final static String WebZskhUrl = "/sysmgr/knowledge/login_Knowledgemgr.action?uid=%1$s&pwd=%2$s";
	// 审批报告
	public final static String WebSpbgUrl = "/sysmgr/workreportmobile/queryAuditReport_workReportMobileNew.action";
	//单个审批报告
	
	public final static String WebDgspUrl = "/sysmgr/workreportmobile/toEditAuditPage_workReportMobileNew.action";
	// 销售下单
	public final static String WebXsXdUrl = "/saleCenter/saleBill/querySalesBill_salesMobile.action?uid=%1$s&pwd=%2$s";
	// 销售单审核
	public final static String WebShUrl = "/production/market/saleCenter/saleBill/mobile/sales_sp_list.html";
	// 我的销售单
	public final static String WebMysaleUrl = "/production/market/saleCenter/saleBill/mobile/mySaleBill_list.html";
	// 服务查询
	public final static String WebFwzUrl = "/custmgr/maplocation/toPhoneFwz" +
			"_MachineLocation.action?userid=%1$s";
	// 机器查询
	public final static String WebATMCUrl = "/custmgr/maplocation/toPhoneMachine_MachineLocation.action?userid=%1$s";
	// 搜索经纬度ATM信息
	public final static String LocATMListUrl = "/custmgr/maplocation/queryLnglatByJqbh_MachineLocation.action";
	// 获取ATM列表
	public final static String ATMListUrl = "/custmgr/maplocation/queryLnglatByJqbhForPhone_MachineLocation.action";
	// 员工定位搜索
	public final static String StaffListUrl = "/custmgr/maplocation/queryUserlistForPhone_MachineLocation.action";
	// 项目管理
	public final static String ProjectUrl = "/projectmanage/projectcollectstatistic/workorderListStatistic.html";
	// 项目工单审批]
	public final static String ProjectAuditUrl = "/projectmanage/auditOrder/mallwaitsubmitorder.html";
	//项目工单销单
	public final static String ProjectDEALORDERUrl = "/projectmanage/dealorder/xdorderlist.html";
	
	// 检索机器
	public final static String CheckMacineUrl = "/mobile/machineMobileMap/checkMacine_machineMobileMapMgr.action";
	// 文件上传files longitude latitude upimage userid viewjson
	public final static String UploadPicUrl = "/mobile/machineMobileMap/saveMachineData_machineMobileMapMgr.action";
	// 检测session是否有效
	public final static String CheckSessionUrl = "/kt/mobile/returnInvalidateSessionByMobile_mobileAction.action";
	// 推送消息点击接收后 回调 jpushSendNo
	public final static String JPushCallBackUrl = "/kt/mobile/jpushCallBack_mobileAction.action";
	// 上传经纬度
	public final static String UploadLocationUrl = "/mobile/machineMobileMap/saveUserLonction_machineMobileMapMgr.action";
	// 注销session
	public final static String LoginoutUrl = "/kt/mobile/exitLogin_mobileAction.action";
	// 极光注册域名是否成功
	public final static String PushUpdateAliasUrl = "/kt/mobile/updateAliasStatus_mobileAction.action";
	// 打开其他模块busid不为空的url
	public final static String OtherBusIdUrl = "/sysmgr/workreport/toViewMyOrderDetailPage_workreportmobile.action?openerflag=1&vo.workOrderId=%s&reportflowstatus=10";
	// 注册极光IMEI
	public final static String RegJPushIMEIUrl = "/kt/mobile/registerMobileUser_mobileAction.action";
	// app升级
	public final static String APPUpdateUrl = "/kt/mobile/update_mobileAction.action";
	// 反馈
	public final static String SubmitFeedBackUrl = "/kt/mobile/saveUserFeedBackInfo_mobileAction.action";;

	//知识库
	public final static String KnowledgeUrl="/sysmgr/knowledge/queryKnowledgeList_KnowledgeMobileMgr.action";
	//通用文件上传接口
	//upload; 上传文件的类型 ContentType  uploadContentType;  上传文件的名称   uploadFileName; 
	//获取费用方式
	public final static String FeeModeUrl="/sysmgr/workreportmobile/getFeeMode_workReportMobileNew.action";
     //是否退回报告
	public final static String SfthUrl="/sysmgr/workreportmobile/doAuditFlow_workReportMobileNew.action";
	//0批量审核 1：批量退回
	public final static String PlthUrl="/sysmgr/workreportmobile/toAll_workReportMobileNew.action";
	//故障描述
	public final static String GzmsUrl="/sysmgr/workreportmobile/getTroubleRemarkInfo_workReportMobileNew.action";
	//处理工程
	public final static String ClgcUrl="/sysmgr/workreportmobile/getWorkTypeHandleSub_workReportMobileNew.action";
	//故障类别
	public final static String GzlbUrl="/sysmgr/workreportmobile/queryWlByAll_workReportMobileNew.action";
	//故障部件
	public final static String GzbjUrl="/sysmgr/workreportmobile/queryWlByAll_workReportMobileNew.action";
	//派单列表
	public final static String PdlbUrl="/workreportmobile/queryAssignOrderInfo_workordermobile.action";
	//机器查询
	public final static String JqcxUrl="/workreportmobile/queryMachineInfoList_workordermobile.action";
	//查询维护人员
	public final static String WhryUrl="/workreportmobile/queryWorkerName_workordermobile.action";
	//获取系统推荐维护人员
	public final static String XttjwhryUrl="/workreportmobile/queryAvailableWorkerListByJQid_workordermobile.action";
	//提交派单
	public final static String TjpdUrl="/workreportmobile/doSaveAssignWorkOrder_workordermobile.action";
	//查询历史派单
	public final static String CxlspdUrl="/workreportmobile/queryAssignOrderInfo_workordermobile.action";
	//查询服务站人员
	public final static String CxfwzryUrl="/workreportmobile/queryFwzWorker_workordermobile.action";
	//webview连接
	public final static String WebViewUrl="/sysmgr/workreport/toEdit_workreportmobile.action";
	//是否退机
	public final static String SftjUrl="/sysmgr/workreportmobile/queryBackSingle_workReportMobileNew.action";
	//查询用户的培训考核试卷
	public final static String sjlistUrl = "/custmgr/skillgeade/querySjPageList_pxkhmgrMobile.action";
	//数据库更新
	public final static String SjkgxUrl="/sysmgr/workreportmobile/checkBasicData_workReportMobileNew.action";
	//选择题
	public final static String XztUrl="/custmgr/skillgeade/xztJsonDataFind_pxkhmgrMobile.action";
	//简答题
	public final static String JdtUrl="/custmgr/skillgeade/jdtJsonDataFind_pxkhmgrMobile.action";
	//填空题
	public final static String TktUrl="/custmgr/skillgeade/tktJsonDataFind_pxkhmgrMobile.action";
	//提交试卷
	public final static String TjsjUrl="/custmgr/skillgeade/doSavePxkhData_pxkhmgrMobile.action";
	//物流监控其他事务 start //
	public final static String WljkotUrl="/custmgr/othertask/queryOtherTask_otherTaskMobile.action";
	public final static String WljkcUrl="/custmgr/othertask/queryOtherConsign_otherTaskMobile.action";
	public final static String WljkstuatsUrl="/custmgr/othertask/updateStatus_otherTaskMobile.action";
	public final static String WljkOtSubmitUrl="/custmgr/othertask/submitOtherTask_otherTaskMobile.action";
	//物流监控其他事务 stop  //
	
}