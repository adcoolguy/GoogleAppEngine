package com.appspot.cloudserviceapi.sci.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cloudserviceapi.app.controller.SortedServiceRegistryRepository;
import tapp.model.ServiceRegistry;

public interface ServiceRegistryRepository extends JpaRepository<ServiceRegistry, Long>, SortedServiceRegistryRepository {
}