package com.boil.dao;

import com.boil.models.MsTask;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by songyu on 2017/5/2.
 */
@Transactional
public interface TaskDao extends CrudRepository<MsTask, Long> {
}
