package com.ppc.mm.nickprodreporting.dao;

import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodreporting.entity.NpNewVersionReport;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;



import java.util.List;

@Repository
@Transactional
public class NewVersionDAOImpl implements NewVersionDAO {

    private static final Logger logger = LoggerFactory.getLogger(NewVersionDAOImpl.class);

    SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    Environment environment;

    @Override
    public List<NpNewVersionReport> getNewVersionReports() {

        List<NpNewVersionReport> resultList = null;

        try (Session session = sessionFactory.openSession()){

            int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("maxResultForNewVersionReport"));

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<NpNewVersionReport> criteria = builder.createQuery(NpNewVersionReport.class);
            Root<NpNewVersionReport> root = criteria.from(NpNewVersionReport.class);

            Predicate statusPredicate = builder.isNull(root.get("status"));
            criteria.select(root).where(statusPredicate);

            criteria.orderBy(builder.asc(root.get("id")));

            Query<NpNewVersionReport> squery = session.createQuery(criteria);

            resultList = squery.setMaxResults(MAX_RESULT).getResultList();

        }catch(Exception e){
            logger.info("Error in getInboundReportMsgs   {} ",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }


        return resultList;
    }

    @Override
    public String getOldUoiId(String logicalUoiId, String version) {

        String oldUoiId = null;
        try (Session session = sessionFactory.openSession()){
            String query = "select uoi_id from uois where logical_uoi_id ='"+logicalUoiId+"' " +
                    "and version="+(Integer.parseInt(version)-1)+"";

            oldUoiId = session.createNativeQuery(query).uniqueResult().toString();
        }catch(Exception e){
            logger.info("Error in getOldUoiId   {} ",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return oldUoiId;
    }

    @Override
    public NickProdAssetMetadata getAssetMetadata(String uoiId) {
        NickProdAssetMetadata assetMetadata;

        try (Session session = sessionFactory.openSession()){

            assetMetadata = session.get(NickProdAssetMetadata.class,uoiId);
        }catch(Exception e){
            logger.info("Error in getNickProdAssetMetadata   {} ",e.toString());
            throw new RuntimeException(e.getMessage());
        }

        return assetMetadata;
    }

    @Override
    public void saveNewVersionReport(NpNewVersionReport mmInboundReport) {
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(mmInboundReport);
            tx.commit();
        } catch(Exception e){
            logger.error("Error in saveNewVersionReport  {} ",e.toString());
        }
    }

    @Override
    public List<String> getFileNamesForProcess(Integer proDId) {
        List<String> names = null;
        int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("maxResultForNewVersionReport"));
        int prodId = Integer.parseInt(environment.getRequiredProperty("prodId"));
        try (Session session = sessionFactory.openSession()){
            String query = "select file_name_compare from nick_file_name_objects where prod_id = "+prodId+" and processed is null and checksum_count = 1 " +
                    "    and uoi_count = 5 and rownum < "+MAX_RESULT;

            names = session.createNativeQuery(query).getResultList();
        }catch(Exception e){
            logger.info("Error in getFileNamesForProcess   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }
        return names;
    }

    @Override
    public void executeProcedure(List<String> nickObjects) {
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            //tx = session.beginTransaction();
            for (String name : nickObjects){
                logger.info("calling for {}",name);
                StoredProcedureQuery query = session.createStoredProcedureQuery("NICK_OBJECTS_UPDATE_NEW")
                        .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
                        .setParameter(1, name);
                query.execute();
                //session.createStoredProcedureCall("NICK_OBJECTS_UPDATE_NEW")
                  //      .registerParameter(1, String.class, ParameterMode.IN).execute();

            }
            //tx.commit();
        } catch(Exception e){
            logger.error("Error in executeProcedure  {} ",e.toString());
        }
    }

    @Override
    public List<String> getUoiIds(String nickObject) {
        List<String> names = null;

        try (Session session = sessionFactory.openSession()){
            String query = "select uoi_id from uois_nick_new where file_name_compare = '"+nickObject+"'  order by import_dt ";

            names = session.createNativeQuery(query).getResultList();
        }catch(Exception e){
            logger.info("Error in getUoiIds {} ",e.getMessage());

        }
        return names;
    }

    @Override
    public void updateUois(String id, int counter, String isLatest, String original) {

        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            String query = "update uois_nick_new set logical_uoi_id = :original,version = :version, is_latest_version = :latest where uoi_id = :id";
            session.createNativeQuery(query).setParameter("original",original)
                    .setParameter("version", counter)
                    .setParameter("latest", isLatest)
                    .setParameter("id", id)
                    .executeUpdate();

            tx.commit();
        } catch(Exception e){
            logger.error("Error in updateUois  {} ",e.toString());
        }
    }

    @Override
    public void insertFileCompareObjects(String id) {
//        insert into nick_file_compare_update
//        select  prod_id, file_name_compare,uoi_id,sysdate from uois_nick_new where uoi_id = mig_id1.uoi_id;

        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            String query = "insert into nick_file_compare_update select  prod_id, file_name_compare,uoi_id,sysdate from uois_nick_new where uoi_id = :id";
            session.createNativeQuery(query)
                    .setParameter("id", id)
                    .executeUpdate();

            tx.commit();
        } catch(Exception e){
            logger.error("Error in insertFileCompareObjects  {} ",e.toString());
        }
    }

    @Override
    public void updateFileNameObjects(String nickObject) {
//        update nick_file_name_objects set processed = 'Y',processed_date = sysdate, versioned = 'Y' where file_name_compare = CHECKSUM;

        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            String query = "update nick_file_name_objects set processed = 'Y',processed_date = sysdate, versioned = 'Y' where file_name_compare = :id";
            session.createNativeQuery(query)
                    .setParameter("id", nickObject)
                    .executeUpdate();

            tx.commit();
        } catch(Exception e){
            logger.error("Error in updateFileNameObjects  {} ",e.toString());
        }
    }
}
