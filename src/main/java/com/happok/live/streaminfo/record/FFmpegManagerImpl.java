package com.happok.live.streaminfo.record;

import com.happok.live.streaminfo.config.FFmpegConfig;
import com.happok.live.streaminfo.dao.record.TaskDao;
import com.happok.live.streaminfo.dao.record.TaskDaoImpl;
import com.happok.live.streaminfo.entity.record.TaskEntity;
import com.happok.live.streaminfo.service.record.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * FFmpeg命令操作管理器
 */

public class FFmpegManagerImpl implements FFmpegManager {

    private static Logger LogUtil = LoggerFactory.getLogger(FFmpegManagerImpl.class);

    /**
     * 任务持久化器
     */
    private TaskDao taskDao = null;
    /**
     * 任务执行处理器
     */
    private TaskHandler taskHandler = null;
    /**
     * 命令组装器
     */
    private CommandAssembly commandAssembly = null;
    /**
     * 任务消息处理器
     */
    private OutHandlerMethod ohm = null;


    public FFmpegManagerImpl(Integer size) {
        init(size);
    }

    public FFmpegManagerImpl(CommandAssembly commandAssembly) {
        this.commandAssembly = commandAssembly;
        init(null);
    }

    public FFmpegManagerImpl() {
        init(null);
    }

    /**
     * 初始化，如果几个处理器未注入，则使用默认处理器
     *
     * @param size
     */
    public void init(Integer size) {

		/*if (config == null) {
			LogUtil.error("配置文件加载失败！配置文件不存在或配置错误");
			return;
		}
		*/
		if (size == null) {
			size = FFmpegConfig.getSize() == null ? 10 : FFmpegConfig.getSize();
		}
        if (this.ohm == null) {
            this.ohm = new DefaultOutHandlerMethod();
        }
        if (this.taskDao == null) {
            this.taskDao = new TaskDaoImpl(size);
        }
        if (this.taskHandler == null) {
            this.taskHandler = new TaskHandlerImpl(this.ohm);
        }
        if (this.commandAssembly == null) {
            this.commandAssembly = new CommandAssemblyImpl();
        }
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public void setTaskHandler(TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    public void setCommandAssembly(CommandAssembly commandAssembly) {
        this.commandAssembly = commandAssembly;
    }

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    /**
     * 是否已经初始化
     *
     * @param 如果未初始化时是否初始化
     * @return
     */
    public boolean isInit(boolean b) {
        boolean ret = this.ohm == null || this.taskDao == null || this.taskHandler == null || this.commandAssembly == null;
        if (ret && b) {
            init(null);
        }
        return ret;
    }

    @Override
    public String start(String id, String command) {
        return start(id, command, false);
    }

    @Override
    public String start(String id, String command, boolean hasPath) {
        if (isInit(true)) {
            LogUtil.error("执行失败，未进行初始化或初始化失败！");
            return null;
        }
        if (id != null && command != null) {
            TaskEntity tasker = taskHandler.process(id, hasPath ? command : FFmpegConfig.getPath() + command);
            if (tasker != null) {
                int ret = taskDao.add(tasker);
                if (ret > 0) {
                    return tasker.getId();
                } else {
                    // 持久化信息失败，停止处理
                    taskHandler.stop(tasker.getProcess(), tasker.getThread());
                    if (FFmpegConfig.isDebug())
                        LogUtil.error("持久化失败，停止任务！");
                }
            }
        }
        return null;
    }

    @Override
    public String start(Map<String, String> assembly) {

        // 参数是否符合要求
        if (assembly == null || assembly.isEmpty() || !assembly.containsKey("appName")) {
            LogUtil.error("参数不正确，无法执行");
            return null;
        }
        String appName = (String) assembly.get("appName");
        if (appName != null && "".equals(appName.trim())) {
            LogUtil.error("appName不能为空");
            return null;
        }
        assembly.put("ffmpegPath", FFmpegConfig.getPath() + "ffmpeg");
        String command = commandAssembly.assembly(assembly);
        if (command != null) {
            return start(appName, command, true);
        }

        return null;
    }

    @Override
    public boolean stop(String id) {
        if (id != null && taskDao.isHave(id)) {
            if (FFmpegConfig.isDebug())
                System.out.println("正在停止任务：" + id);
            TaskEntity tasker = taskDao.get(id);
            if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
                taskDao.remove(id);
                return true;
            }
        }
        LogUtil.error("停止任务失败！id=" + id);
        return false;
    }

    @Override
    public int stopAll() {
        Collection<TaskEntity> list = taskDao.getAll();
        Iterator<TaskEntity> iter = list.iterator();
        TaskEntity tasker = null;
        int index = 0;
        while (iter.hasNext()) {
            tasker = iter.next();
            if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
                taskDao.remove(tasker.getId());
                index++;
            }
        }
        if (FFmpegConfig.isDebug())
            System.out.println("停止了" + index + "个任务！");
        return index;
    }

    @Override
    public TaskEntity query(String id) {
        return taskDao.get(id);
    }

    @Override
    public Collection<TaskEntity> queryAll() {
        return taskDao.getAll();
    }
}
