package com.example.demo.services;

import com.example.demo.models.Storage;
import com.example.demo.repo.ProductRepository;
import com.example.demo.repo.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@PreAuthorize("hasAuthority('MODERATOR')")
public class ProductService {
    @Autowired
    private StorageRepository storageRepository;
    public int getCountOfStorage(long id){
        Storage storage = storageRepository.getAllByProductId(id);
        return storage.getQuantity();
    }

}
