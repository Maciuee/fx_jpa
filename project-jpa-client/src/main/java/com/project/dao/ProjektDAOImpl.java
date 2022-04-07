package com.project.dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.project.datasource.JPAUtil;
import com.project.model.Projekt;

public class ProjektDAOImpl implements ProjektDAO {
 
	@Override
 public Projekt getProjekt(Integer projektId) {
 Projekt projekt = null;
 EntityManager entityManager = JPAUtil.getEntityManager();
 
 try {
 projekt = entityManager.find(Projekt.class, projektId);
 
 } finally {
 entityManager.close();
 }
 return projekt;
 }

 @Override
public void setProjekt(Projekt projekt) {
 // TODO
}

 @Override
public void deleteProjekt(Integer projektId) {
	 Projekt projekt = null;
	 EntityManager entityManager = JPAUtil.getEntityManager();
	 
	 try {
	 projekt = entityManager.find(Projekt.class, projektId);
	 entityManager.remove(projekt);
	 
	 } finally {
	 entityManager.close();
	 }
}

 @Override
public List<Projekt> getProjekty(Integer offset, Integer limit) {
	 EntityManager entityManager = JPAUtil.getEntityManager();
	 TypedQuery<Projekt> query = entityManager.createQuery("SELECT p FROM Projekt p ORDER BY p.dataCzasUtworzenia DESC", Projekt.class); query.setFirstResult(offset); query.setMaxResults(limit);
	 List<Projekt> projekty = query.getResultList();
	 return null;
}

 @Override
public List<Projekt> searchByNazwa(String search4, Integer offset, Integer limit) {
 // TODO
 return null;
}
}