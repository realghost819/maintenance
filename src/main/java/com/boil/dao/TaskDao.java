package com.boil.dao;

import com.boil.models.MsTask;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by songyu on 2017/5/2.
 */
public interface TaskDao extends CrudRepository<MsTask, Long> {
}
