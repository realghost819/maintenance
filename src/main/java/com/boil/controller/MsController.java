package com.boil.controller;

import com.boil.dao.ProjectDao;
import com.boil.dao.TaskDao;
import com.boil.models.MsTask;
import com.boil.models.Project;
import com.boil.service.MsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by songyu on 2017/5/2.
 */
@Controller
public class MsController {


    @Autowired
    private TaskDao taskDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private MsService msService;

    @RequestMapping("/scan")
    @ResponseBody
    public String scan(long id) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> scanTask = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                MsTask task = new MsTask(id, "scan");
                taskDao.save(task);
                TimeUnit.SECONDS.sleep(10);

                try {
                    Project project = projectDao.findOne(id);
                    msService.scanProject(project,task);
                    task.success();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    task.fail();
                }


                taskDao.save(task);
                return 1;
            }
        };
        executor.submit(scanTask);
        return "this is a scan task";
    }
}

