package cn.uncode.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import cn.uncode.schedule.zk.TaskDefine;



public class ConsoleManager {   
    protected static transient Logger log = LoggerFactory.getLogger(ConsoleManager.class);

    private static ZKScheduleManager scheduleManager;
    
    public static ZKScheduleManager getScheduleManager() throws Exception {
    	if(null == ConsoleManager.scheduleManager){
    		ConsoleManager.scheduleManager = (ZKScheduleManager)ContextLoader.getCurrentWebApplicationContext().getBean(ZKScheduleManager.class);
    	}
        return ConsoleManager.scheduleManager;
    }

    public static void addScheduleTask(TaskDefine taskDefine) {
        try {
			ConsoleManager.scheduleManager.getScheduleDataManager().addTask(taskDefine);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    }
    
    public static void delScheduleTask(String targetBean, String targetMethod) {
        try {
			ConsoleManager.scheduleManager.getScheduleDataManager().delTask(targetBean, targetMethod);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    }
    
    public static String[] queryScheduleTask() {
        try {
			return ConsoleManager.scheduleManager.getScheduleDataManager().selectTask();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        return null;
    }
    
}
