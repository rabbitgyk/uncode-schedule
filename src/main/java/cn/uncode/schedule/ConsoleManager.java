package cn.uncode.schedule;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.uncode.schedule.core.TaskDefine;
import cn.uncode.schedule.util.ScheduleUtil;



public class ConsoleManager {
	
    private static transient Logger log = LoggerFactory.getLogger(ConsoleManager.class);
    
//    private static Gson GSON = new GsonBuilder().create();

    private static ZKScheduleManager scheduleManager;
    
    public static ZKScheduleManager getScheduleManager() throws Exception {
    	if(null == ConsoleManager.scheduleManager){
			synchronized(ConsoleManager.class) {
				ConsoleManager.scheduleManager = ZKScheduleManager.getApplicationcontext().getBean(ZKScheduleManager.class);
			}
    	}
        return ConsoleManager.scheduleManager;
    }

    public static void addScheduleTask(TaskDefine taskDefine) {
        try {
			ConsoleManager.getScheduleManager().getScheduleDataManager().addTask(taskDefine);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    }
    
    public static void delScheduleTask(TaskDefine taskDefine) {
        try {
			ConsoleManager.scheduleManager.getScheduleDataManager().delTask(taskDefine);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    }
    
    public static List<TaskDefine> queryScheduleTask() {
    	List<TaskDefine> taskDefines = new ArrayList<TaskDefine>();
        try {
			List<TaskDefine> tasks = ConsoleManager.getScheduleManager().getScheduleDataManager().selectTask();
			taskDefines.addAll(tasks);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        return taskDefines;
    }
    
    public static boolean isExistsTask(TaskDefine taskDefine) throws Exception{
    		return ConsoleManager.scheduleManager.getScheduleDataManager().isExistsTask(taskDefine);
    }
    
    /**
     * 手动执行定时任务
     * @param task
     */
    public static void runTask(TaskDefine task){
		Object object = null;
		if (StringUtils.isNotEmpty(task.getTargetBean())) {
			object = ZKScheduleManager.getApplicationcontext().getBean(task.getTargetBean());
		}
		if (object == null) {
			log.error("任务名称 = [{}]---------------未启动成功，请检查是否配置正确！！！", task.getTargetBean());
			return;
		}
		Method method = null;
		try {
			//method = object.getClass().getDeclaredMethod(task.getTargetMethod(), String.class);
			method = object.getClass().getDeclaredMethod(task.getTargetMethod());
		} catch (Exception e) {
			log.error(String.format("定时任务bean[%s]，method[%s]初始化失败.", task.getTargetBean(), task.getTargetMethod()), e);
		}
		if (method != null) {
			try {
				//method.invoke(object, scheduleJob.getJobName());
				method.invoke(object);
			} catch (Exception e) {
				log.error(String.format("定时任务bean[%s]，method[%s]调用失败.", task.getTargetBean(), task.getTargetMethod()), e);
			}
		}
		log.info("任务名称 = [{}]----------启动成功", ScheduleUtil.getTaskNameFormBean(task.getTargetBean(), task.getTargetMethod()));
	}
    
}
