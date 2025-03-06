package com.ppc.mm.nickprodmetacomparing.dao;

import com.ppc.mm.nickprodmetacomparing.entity.NickMetadataCompare;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetadataUpdateMarch25;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class MetadataDaoImpl implements MetadataDao {


    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    Environment environment;

    private static final Logger log = LoggerFactory.getLogger(MetadataDaoImpl.class);

    @Override
    public List<NickMetadataCompare> getFilesForMetadataCompare() {
        log.info(" in dao getFilesForMetadataCompare");

        List<NickMetadataCompare> nickMetadataCompares = null;

        try (Session session = sessionFactory.openSession()){


            int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("maxResultForMetadataCompare"));

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<NickMetadataCompare> criteria = builder.createQuery(NickMetadataCompare.class);
            Root<NickMetadataCompare> root = criteria.from(NickMetadataCompare.class);

            Predicate statusPredicate = builder.isNull(root.get("processed"));
            criteria.select(root).where(statusPredicate);

            criteria.orderBy(builder.asc(root.get("id")));

            Query<NickMetadataCompare> squery = session.createQuery(criteria);

            nickMetadataCompares = squery.setMaxResults(MAX_RESULT).getResultList();

        }catch(Exception e){
            log.info("Error in getFilesForMetadataCompare   {} ",e);
            //throw new RuntimeException(e.getMessage());
        }

        return nickMetadataCompares;

    }

    @Override
    public String getUoiId(String filename, Long productionId) {

        log.info(" in dao getUoiId");
        String result = null;
        List<NickMetadataCompare> nickMetadataCompares = null;

        try (Session session = sessionFactory.openSession()){

            //String query =







        }catch(Exception e){
            log.info("Error in getUoiId    ",e);
        }

        return result;
    }

    @Override
    public List<Object[]> getUoiForMerge() {
        log.info(" in dao getUoiForMerge");

        List<Object[]> nickMetadataCompares = null;

        try (Session session = sessionFactory.openSession()){

            int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("maxResultForMetadataCompare"));
            String query = "select uoi_id,logical_uoi_id,version,is_latest_version from uois_nick_new  where rematched = 'N' and processed is null " +
                    "   and rownum < "+MAX_RESULT;


            nickMetadataCompares = session.createNativeQuery(query).getResultList();

        }catch(Exception e){
            log.info("Error in getUoiForMerge   {} ",e);
        }

        return nickMetadataCompares;
    }

    @Override
    public void updateUois(String uoiId, String logicalUoiId, String version, String isLatestVersion) {

        log.info(" in dao updateUois");

        try (Session session = sessionFactory.openSession()){

            Transaction tx = session.beginTransaction();

            StringBuilder builder = new StringBuilder();

            builder.append(" update uois u set logical_uoi_id ='");
            builder.append(logicalUoiId);
            builder.append("',");
            builder.append(" version = ");
            builder.append(version);
            builder.append(",");
            builder.append("is_latest_version = '");
            builder.append(isLatestVersion);
            builder.append("'");
            builder.append(" where uoi_id = '");
            builder.append(uoiId);
            builder.append("'");

            /*builder.append(" update uois u set (logical_uoi_id,version, is_latest_version) ");
            builder.append(" = (select logical_uoi_id,version, is_latest_version from uois_nick_new un ");
            builder.append(" where u.uoi_id = un.uoi_id and un.uoi_id = '");
            builder.append(uoiId);
            builder.append("') ");
            builder.append(" where exists ");
            builder.append(" (select logical_uoi_id,version, is_latest_version from uois_nick_new un ");
            builder.append(" where u.uoi_id = un.uoi_id and un.uoi_id = '");
            builder.append(uoiId);
            builder.append("') ");*/
            session.createNativeQuery(builder.toString()).executeUpdate();

            tx.commit();

        }catch(Exception e){
            log.info("Error in updateUois   {} ",e);
            //throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addToIndexer(String uoiId) {
        log.info("addToIndexer");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){

            transaction = session.beginTransaction();

                String updateOriginal = "Insert into INDEX_WORKQUEUE Values (INDEX_WORKQUEUE_ID_SEQ.nextval, :objectId, 'UPDATE',113, 'NONE',sysdate)";

                session.createNativeQuery(updateOriginal).setParameter("objectId", uoiId).executeUpdate();


            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            log.error("Error in addToIndexer   {} ",e.getMessage());

        }
    }

    @Override
    public void updateUoisNick(String uoiId) {
        log.info("addToIndexer");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){

            transaction = session.beginTransaction();

            String updateOriginal = "update uois_nick_new set processed = 'Y' where uoi_id = :objectId ";

            session.createNativeQuery(updateOriginal).setParameter("objectId", uoiId).executeUpdate();


            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            log.error("Error in addToIndexer   {} ",e.getMessage());

        }
    }

    @Override
    public List<NickProdMetadataUpdateMarch25> getMetaDumpData() {
        log.info(" in dao getMetaDumpData");

        List<NickProdMetadataUpdateMarch25> nickProdMetadataUpdateMarch25List = null;

        try (Session session = sessionFactory.openSession()){


            int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("maxResultForMetadataCompare"));

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<NickProdMetadataUpdateMarch25> criteria = builder.createQuery(NickProdMetadataUpdateMarch25.class);
            Root<NickProdMetadataUpdateMarch25> root = criteria.from(NickProdMetadataUpdateMarch25.class);

            Predicate statusPredicate = builder.isNull(root.get("processed"));
            criteria.select(root).where(statusPredicate);

            criteria.orderBy(builder.asc(root.get("id")));

            Query<NickProdMetadataUpdateMarch25> squery = session.createQuery(criteria);

            nickProdMetadataUpdateMarch25List = squery.setMaxResults(MAX_RESULT).getResultList();

        }catch(Exception e){
            log.info("Error in getMetaDumpData   {} ",e);
            //throw new RuntimeException(e.getMessage());
        }

        return nickProdMetadataUpdateMarch25List;

    }

    @Override
    public void saveObject(NickProdMetadataUpdateMarch25 nickObject) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.saveOrUpdate(nickObject);
            transaction.commit();
        } catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            log.error("Error in saveObject  {} ",e.getMessage());
        }
    }

    @Override
    public Integer getId(String condition, String tableName, String columnSelect, String searchCol) {

        try (Session session = sessionFactory.openSession();){
            String sqlQuery = "select "+columnSelect+" from "+tableName+" where upper("+searchCol+") = '"+condition+"'";
            return (Integer) session.createSQLQuery(sqlQuery).addScalar(columnSelect, IntegerType.INSTANCE).uniqueResult();

        } catch (Exception e) {
            log.error("Error in getId =  {}",e.getMessage());
        }
        return 0;
    }
}
