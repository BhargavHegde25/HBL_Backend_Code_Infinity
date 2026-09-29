package com.temenos.dbx.product.api;

import java.util.Map;

import com.dbp.core.api.DBPDTO;

public interface DBPDTOEXT extends DBPDTO {

   
    public boolean persist(Map<String, Object> input, Map<String, Object> headers);

    public Object loadDTO(String id);
    
    public Object loadDTO();

}
