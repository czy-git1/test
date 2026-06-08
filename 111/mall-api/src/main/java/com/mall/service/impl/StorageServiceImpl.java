package com.mall.service.impl;

import com.mall.dao.mapper.StorageMapper;
import com.mall.dao.model.Product;
import com.mall.dao.model.Storage;
import com.mall.exception.StorageException;
import com.mall.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageMapper storageMapper;

    @Override
    public void initStorage(Product product) {
        Storage storage = new Storage();
        storage.setProductId(product.getProductId());
        storage.setCreateTime(new Date());
        storage.setInOut(1);
        storage.setRemark("新商品上架");
        storage.setQuantity(product.getQuantity());
        storage.setNewQuantity(product.getQuantity());
        storageMapper.insertSelective(storage);
    }

    @Override
    public void addStorage(Integer productId, int quantity, String remark) {
        List<Storage> list = storageMapper.selectLastStorageByProductId(productId);
        Storage storage1 = new Storage();
        storage1.setProductId(productId);
        storage1.setCreateTime(new Date());
        storage1.setInOut(1);
        storage1.setQuantity(quantity);
        storage1.setRemark(remark);
        if(list!=null && !list.isEmpty()){
            Storage storage = list.get(0);
            int lastQuantity = storage.getNewQuantity();
            storage1.setNewQuantity(lastQuantity+quantity);
        }else{
            storage1.setNewQuantity(quantity);
        }
        storageMapper.insertSelective(storage1);
    }

    @Override
    public void minusStorage(Integer productId, int quantity, String remark) throws StorageException {
        List<Storage> list = storageMapper.selectLastStorageByProductId(productId);
        Storage storage1 = new Storage();
        storage1.setProductId(productId);
        storage1.setCreateTime(new Date());
        storage1.setInOut(-1);
        storage1.setQuantity(quantity);
        storage1.setRemark(remark);
        if(list!=null && !list.isEmpty()){
            Storage storage = list.get(0);
            int lastQuantity = storage.getNewQuantity();
            if(lastQuantity < quantity){
                throw new StorageException("库存不足！");
            }
            storage1.setNewQuantity(lastQuantity-quantity);
        }else{
            storage1.setNewQuantity(quantity);
        }
        storageMapper.insertSelective(storage1);
    }

    @Override
    public void stocktaking(Integer productId, int quantity, String remark) {
        Storage storage1 = new Storage();
        storage1.setProductId(productId);
        storage1.setCreateTime(new Date());
        storage1.setInOut(0);
        storage1.setQuantity(quantity);
        storage1.setNewQuantity(quantity);
        storage1.setRemark(remark);
        storageMapper.insertSelective(storage1);
    }

    @Override
    public Storage findLastStorage(Integer productId) {
        List<Storage> list = storageMapper.selectLastStorageByProductId(productId);
        if(list!=null && !list.isEmpty()){
            return list.get(0);
        }else{
            return null;
        }
    }
}
