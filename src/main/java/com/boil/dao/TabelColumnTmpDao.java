package com.boil.dao;

import com.boil.models.DbObjectTmp;
import com.boil.models.TableColumnTmp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by songyu on 2017/5/2.
 */
@Transactional
public interface TabelColumnTmpDao extends CrudRepository<TableColumnTmp, Long> {

    @Modifying
    @Query("DELETE FROM TableColumnTmp c where c.project_id=:project_id")
    void deleteByProjectId(@Param("project_id") Long project_id);
}
