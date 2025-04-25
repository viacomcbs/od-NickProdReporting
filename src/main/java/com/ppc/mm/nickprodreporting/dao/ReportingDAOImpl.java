package com.ppc.mm.nickprodreporting.dao;

import com.ppc.mm.common.util.Constants;
import com.ppc.mm.nickprodmessaging.entity.AssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata2;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetaValidation25;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetadataUpdateMarch25;
import com.ppc.mm.nickprodreporting.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class ReportingDAOImpl extends AbstractDAO implements ReportingDAO{


    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(ReportingDAOImpl.class);

    @Override
    public List<MMInboundReport> getInboundReportMsgs(String migrated) {

        List<MMInboundReport> mmInboundMsgs;

        try (Session session = sessionFactory.openSession()){


            int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("maxResult"));

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MMInboundReport> criteria = builder.createQuery(MMInboundReport.class);
            Root<MMInboundReport> root = criteria.from(MMInboundReport.class);

            Predicate migratePredicate = builder.equal(root.get("migrated"), migrated);
            Predicate statusPredicate = builder.isNull(root.get("status"));
            if (migrated.equals(Constants.YES)) {
                //MAX_RESULT = 1;
                criteria.select(root).where(migratePredicate,statusPredicate);
            } else {
                criteria.select(root).where(migratePredicate);
            }
            criteria.orderBy(builder.asc(root.get("id")));

            Query<MMInboundReport> squery = session.createQuery(criteria);

            mmInboundMsgs = squery.setMaxResults(MAX_RESULT).getResultList();

        }catch(Exception e){
            logger.info("Error in getInboundReportMsgs   {} ",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return mmInboundMsgs;
    }

    @Override
    public void saveTabular(List<MMInboundTabularReport> tabularReports) {
        logger.info("saveTabular ");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            for (MMInboundTabularReport report : tabularReports){
                session.save(report);
            }
            transaction.commit();
        }catch(Exception e){
            logger.info("Error in saveTabular   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void saveEntity(NickProdUploadMsgEntity entity) {
        logger.info("in dao saveEntity");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

            session.saveOrUpdate(entity);

            transaction.commit();
        }catch(Exception e){
            logger.info("Error in saveEntity    ",e);
            //throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void saveReport(MMInboundReport report) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
        /*    boolean update = false;
            String query = "Update MM_INBOUND_REPORT set ";
            if (StringUtils.isNotBlank(report.getStatus())){
               query += " status = '"+report.getStatus()+"' ";
               update = true;
            }

            if (StringUtils.isNotBlank(report.getFieldsValid())){
                if (update){
                    query += " ,FIELDS_VALID ='"+report.getFieldsValid()+"'";
                }else {
                    query += " FIELDS_VALID ='"+report.getFieldsValid()+"'";
                    update =true;
                }

            }
            if (StringUtils.isNotBlank(report.getErrorMsg())){
                if (update){
                    query += " ,ERROR_MSG ='"+report.getErrorMsg()+"'";
                }else {
                    query += " ERROR_MSG ='"+report.getErrorMsg()+"'";
                    update = true;
                }
            } else {

                if (update){
                    query += " ,ERROR_MSG ='"+report.getErrorMsg()+"'";
                }else {
                    query += " ERROR_MSG ='"+report.getErrorMsg()+"'";
                    update = true;
                }
            }

            query += " where id="+report.getId();

            if (update){
                logger.info(query);
                transaction = session.beginTransaction();
                session.createNativeQuery(query).executeUpdate();
                //session.update(report);
                transaction.commit();
            }*/
            logger.info("updating report");
            transaction = session.beginTransaction();
            //session.createNativeQuery(query).executeUpdate();
            session.update(report);
            transaction.commit();
        }catch(Exception e){
            logger.info("Error in saveReport   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void saveResponse(MMInboundResponse inboundResponse) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

            session.saveOrUpdate(inboundResponse);

            transaction.commit();
        }catch(Exception e){
            logger.info("Error in saveResponse   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getById(Integer id, Class<?> clazz) {
        return get(id, clazz);
    }

    @Override
    public List<String> getUoiId(String fileName) {
        List<String> uoids = new ArrayList<>();
        String uoi_id = null;
        try (Session session = sessionFactory.openSession()){

            String query = "Select uoi_id from uois where name = '"+fileName+"' and is_latest_version = 'Y' ORDER BY IMPORT_DT DESC";
            uoids = session.createNativeQuery(query).getResultList();

        }catch(Exception e){
            logger.info("Error in getUoiId   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());
            //return Constants.NO_UNIQUE;
        }
        return uoids;
    }

    @Override
    public NickProdUploadMsgEntity getByQueueId(Integer id) {

        NickProdUploadMsgEntity uploadMsgEntity;

        try (Session session = sessionFactory.openSession()){

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<NickProdUploadMsgEntity> criteria = builder.createQuery(NickProdUploadMsgEntity.class);
            Root<NickProdUploadMsgEntity> root = criteria.from(NickProdUploadMsgEntity.class);

            Predicate statusPPredicate = builder.equal(root.get("queueId"), id);

            criteria.select(root).where(statusPPredicate);

            Query<NickProdUploadMsgEntity> squery = session.createQuery(criteria);
            criteria.orderBy(builder.asc(root.get("id")));
            uploadMsgEntity = squery.setMaxResults(1).uniqueResult();


        }catch(Exception e){
            logger.info("Error in getByQueueId   {} ",e.toString());
            throw new RuntimeException(e.getMessage());
        }

        return uploadMsgEntity;

    }

    @Override
    public MMInboundResponse getMMInboundResponse(Integer id) {


        MMInboundResponse inboundResponse;

        try (Session session = sessionFactory.openSession()){

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MMInboundResponse> criteria = builder.createQuery(MMInboundResponse.class);
            Root<MMInboundResponse> root = criteria.from(MMInboundResponse.class);

            Predicate statusPPredicate = builder.equal(root.get("mmQueueId"), id);

            criteria.select(root).where(statusPPredicate);

            Query<MMInboundResponse> squery = session.createQuery(criteria);
            criteria.orderBy(builder.asc(root.get("id")));
            inboundResponse = squery.setMaxResults(1).uniqueResult();


        }catch(Exception e){
            logger.info("Error in getMMInboundResponse   {} ",e.toString());
            throw new RuntimeException(e.getMessage());
        }

        return inboundResponse;

    }

    @Override
    public List<MMInboundTabularReport> getMMTabularReports(Integer id) {
        return List.of();
    }

    @Override
    public int countTabValue(String column, String addValue, String uoiId, String table) {

        logger.info("<<<<<<checkImportInProgress>>>>");
        int count = 0;
        BigDecimal countBig;
        StringBuilder builder = new StringBuilder();
        builder.append("select count(*) from ");
        builder.append(table);
        builder.append(" where ");
        builder.append(column);
        builder.append(" ="+addValue+" and ");
        builder.append(" uoi_id ="+uoiId);

        try (Session hibernateSession = sessionFactory.openSession()){
            NativeQuery sqlObj = hibernateSession.createNativeQuery(builder.toString());
            countBig = (BigDecimal) sqlObj.uniqueResult();
            count = countBig.intValueExact();

        } catch (Exception e) {
            logger.error("ERROR : checkImportInProgress : ", e);
        }
        return count;
    }

    @Override
    public AssetMetadata getAssetMetadata(String uoiId) {


        AssetMetadata assetMetadata;

        try (Session session = sessionFactory.openSession()){

            assetMetadata = session.get(AssetMetadata.class,uoiId);
        }catch(Exception e){
            logger.info("Error in getAssetMetadata   {} ",e.toString());
            throw new RuntimeException(e.getMessage());
        }

        return assetMetadata;

    }

    @Override
    public NickProdAssetMetadata2 getNickProdAdditionalMetadata(String uoiId) {
        NickProdAssetMetadata2 assetMetadata;

        try (Session session = sessionFactory.openSession()){

            assetMetadata = session.get(NickProdAssetMetadata2.class,uoiId);
        }catch(Exception e){
            logger.info("Error in getNickProdAdditionalMetadata   {} ",e.toString());
            throw new RuntimeException(e.getMessage());
        }

        return assetMetadata;
    }

    @Override
    public NickProdAssetMetadata getNickProdAssetMetadata(String uoiId) {
        NickProdAssetMetadata assetMetadata = null;

        try (Session session = sessionFactory.openSession()){

            assetMetadata = session.get(NickProdAssetMetadata.class,uoiId);
        }catch(Exception e){
            logger.info("Error in getNickProdAssetMetadata   {} ",e.toString());
            //throw new RuntimeException(e.getMessage());
        }

        return assetMetadata;
    }

    @Override
    public List<MMInboundReport> getgetFailedReports(String invalidResponse) {

        List<MMInboundReport> mmInboundMsgs;

        try (Session session = sessionFactory.openSession()){


            int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("maxResult"));

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MMInboundReport> criteria = builder.createQuery(MMInboundReport.class);
            Root<MMInboundReport> root = criteria.from(MMInboundReport.class);

            Predicate statusPredicate = builder.equal(root.get("status"), Constants.FAILED);

            if (!invalidResponse.equals(Constants.ALL)){
                Predicate errorPredicate = builder.equal(root.get("errorMsg"), invalidResponse);
                criteria.select(root).where(statusPredicate,errorPredicate);

            }else {
                criteria.select(root).where(statusPredicate);
            }

            criteria.orderBy(builder.asc(root.get("id")));

            Query<MMInboundReport> squery = session.createQuery(criteria);
            mmInboundMsgs = squery.setMaxResults(MAX_RESULT).getResultList();

        }catch(Exception e){
            logger.info("Error in getFailedReports   {} ",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return mmInboundMsgs;
    }

    @Override
    public List<MMInboundResponse> getMMInboundResponseList(Integer id) {

        List<MMInboundResponse> inboundResponseList;

        try (Session session = sessionFactory.openSession()){

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MMInboundResponse> criteria = builder.createQuery(MMInboundResponse.class);
            Root<MMInboundResponse> root = criteria.from(MMInboundResponse.class);

            Predicate statusPPredicate = builder.equal(root.get("mmQueueId"), id);

            criteria.select(root).where(statusPPredicate);

            Query<MMInboundResponse> squery = session.createQuery(criteria);
            criteria.orderBy(builder.asc(root.get("id")));
            inboundResponseList = squery.getResultList();


        }catch(Exception e){
            logger.info("Error in getMMInboundResponseList   {} ",e.toString());
            throw new RuntimeException(e.getMessage());
        }

        return inboundResponseList;

    }

    @Override
    public void removeObject(MMInboundResponse mmInboundResponse) {
        remove(mmInboundResponse);
    }

    @Override
    public List<String> isFileNameExist(String fileName, String prodID) {
        List<String> uoids = new ArrayList<>();
        String uoi_id = null;
        try (Session session = sessionFactory.openSession()){

            String query = "select uoi_id from (select distinct u.uoi_id, u.IMPORT_DT from uois u join NICK_PROD_ASSET_METADATA np on u.UOI_ID = np.UOI_ID where lower(np.FILE_NAME_COMPARE) = :fileName and np.PROD_ID = :productionId order by u.IMPORT_DT desc)";
            uoids =  session.createNativeQuery(query).setParameter("fileName", fileName.toLowerCase())
                    .setParameter("productionId", prodID).getResultList();


        }catch(Exception e){
            logger.info("Error in isFileNameExist   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());
            //return Constants.NO_UNIQUE;
        }
        return uoids;
    }

    @Override
    public List<NickObjects> getNickObjects(String migrated) {


        List<NickObjects> nickObjects;

        try (Session session = sessionFactory.openSession()){


            int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("maxResult"));

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<NickObjects> criteria = builder.createQuery(NickObjects.class);
            Root<NickObjects> root = criteria.from(NickObjects.class);

            Predicate migratePredicate = builder.isNull(root.get("processed"));
            Predicate count = builder.equal(root.get("uoiCount"), Constants.TWO);
            Predicate statusPredicate = builder.equal(root.get("processed"), Constants.YES);
            Predicate isDuplicate = builder.equal(root.get("isDuplicate"), Constants.YES);
            Predicate errorPredicate = builder.isNull(root.get("errorMsg"));
            Predicate isNonDuplicate = builder.equal(root.get("isDuplicate"), Constants.NO);
            Predicate isVersioned = builder.isNull(root.get("versioned"));

            if (migrated.equals(Constants.YES)) {
                //MAX_RESULT = 1;
                criteria.select(root).where(statusPredicate, isDuplicate, errorPredicate);
            } else if (migrated.equals("NonDup")) {
                criteria.select(root).where(statusPredicate, isNonDuplicate, isVersioned);
            } else {
                criteria.select(root).where(migratePredicate,count);
            }
            criteria.orderBy(builder.asc(root.get("objectId")));

            Query<NickObjects> squery = session.createQuery(criteria);

            nickObjects = squery.setMaxResults(MAX_RESULT).getResultList();

        }catch(Exception e){
            logger.info("Error in getNickObjects   {} ",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return nickObjects;
    }

    @Override
    public void saveObject(NickObjects nickObject) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){

            transaction = session.beginTransaction();

            session.saveOrUpdate(nickObject);

            transaction.commit();

        } catch (Exception e) {
            logger.error("Error in saveObject   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Object[]> getNickObjectList(String objectId) {
        List<Object[]> nickObjects = null;

        try (Session session = sessionFactory.openSession()){
            String query = "select uoi_id,name,import_dt from uois_nick_new where content_checksum  = :objectId order by import_dt";
            nickObjects = session.createNativeQuery(query).setParameter("objectId", objectId).getResultList();
        } catch (Exception e) {
            logger.error("Error in getNickObjectList   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());

        }
        return nickObjects;
    }

    @Override
    public void updateUois(String originalAsset, String updatedAsset) {
        logger.info("updateUois");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

//            String updateOriginal = "update uois set is_latest_version = 'N' where uoi_id = :objectId";
            String updateOriginal = "update uois_nick_new set is_latest_version = 'N' where uoi_id = :objectId";
            session.createNativeQuery(updateOriginal).setParameter("objectId", originalAsset).executeUpdate();

//            String updateLatest = "update uois set logical_uoi_id=:originalAsset, version=2, is_latest_version = 'Y' where uoi_id = :updatedAsset";
            String updateLatest = "update uois_nick_new set logical_uoi_id=:originalAsset, version=2, is_latest_version = 'Y' where uoi_id = :updatedAsset";
            session.createNativeQuery(updateLatest).setParameter("originalAsset", originalAsset).setParameter("updatedAsset", updatedAsset).executeUpdate();


            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            logger.error("Error in updateUois   {} ",e.getMessage());

        }
    }

    @Override
    public void addToIndexer( List<String> assets) {
        logger.info("addToIndexer");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){

            transaction = session.beginTransaction();

            for (String asset : assets) {
                String updateOriginal = "Insert into INDEX_WORKQUEUE Values (INDEX_WORKQUEUE_ID_SEQ.nextval, :objectId, 'UPDATE',113, 'NONE',sysdate)";

                session.createNativeQuery(updateOriginal).setParameter("objectId", asset).executeUpdate();

            }

            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            logger.error("Error in addToIndexer   {} ",e.getMessage());

        }
    }

    @Override
    public Integer getVersoinedNickObjects(String objectId) {

        Integer count = 0;
        BigDecimal result;
        try (Session session = sessionFactory.openSession()){

            String query = "select count(distinct version) from uois_nick_new where content_checksum  = :objectId order by import_dt";
            result = (BigDecimal) session.createNativeQuery(query).setParameter("objectId", objectId).uniqueResult();
            count = result.intValue();
        } catch (Exception e) {
            logger.error("Error in getNickObjectList   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());

        }
        return count;
    }

    @Override
    public List<NickObjects> getNickObjectsForReprocess(String yes) {


        List<NickObjects> nickObjects;

        try (Session session = sessionFactory.openSession()){


            int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("reprocess"));

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<NickObjects> criteria = builder.createQuery(NickObjects.class);
            Root<NickObjects> root = criteria.from(NickObjects.class);

            Predicate errorPredicate = builder.equal(root.get("errorMsg"),"Not Versioned");

            criteria.select(root).where( errorPredicate);

            criteria.orderBy(builder.asc(root.get("objectId")));

            Query<NickObjects> squery = session.createQuery(criteria);

            nickObjects = squery.setMaxResults(MAX_RESULT).getResultList();

        }catch(Exception e){
            logger.info("Error in getNickObjectsForReprocess   {} ",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return nickObjects;
    }

    @Override
    public void updateUoisNew(String uoiId1, String uoiId2) {
        logger.info("updateUoisNew");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

//            String updateOriginal = "update uois set is_latest_version = 'N' where uoi_id = :objectId";
            String updateOriginal = "update uois_nick_new set logical_uoi_id=:objectId, is_latest_version = 'N', version = 1 where uoi_id = :objectId";
            session.createNativeQuery(updateOriginal).setParameter("objectId", uoiId1).executeUpdate();

//            String updateLatest = "update uois set logical_uoi_id=:originalAsset, version=2, is_latest_version = 'Y' where uoi_id = :updatedAsset";
            String updateLatest = "update uois_nick_new set logical_uoi_id=:originalAsset, version=2, is_latest_version = 'Y' where uoi_id = :updatedAsset";
            session.createNativeQuery(updateLatest).setParameter("originalAsset", uoiId1).setParameter("updatedAsset", uoiId2).executeUpdate();


            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            logger.error("Error in updateUoisNew   {} ",e.getMessage());

        }
    }

    @Override
    public List<NickProdMetaValidation25> getValidationReport() {

        List<NickProdMetaValidation25> reports = null;

        try (Session session = sessionFactory.openSession()){


            int MAX_RESULT = Integer.parseInt(environment.getRequiredProperty("reprocess"));

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<NickProdMetaValidation25> criteria = builder.createQuery(NickProdMetaValidation25.class);
            Root<NickProdMetaValidation25> root = criteria.from(NickProdMetaValidation25.class);

            Predicate processed = builder.isNull(root.get("processed"));

            criteria.select(root).where( processed);

            criteria.orderBy(builder.asc(root.get("id")));

            Query<NickProdMetaValidation25> squery = session.createQuery(criteria);

            reports = squery.setMaxResults(MAX_RESULT).getResultList();

        }catch(Exception e){
            logger.info("Error in getValidationReport   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }

        return reports;
    }

    @Override
    public void saveObject(NickProdMetaValidation25 nickObject) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){

            transaction = session.beginTransaction();

            session.saveOrUpdate(nickObject);

            transaction.commit();

        } catch (Exception e) {
            logger.error("Error in saveObject   {} ",e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void updateMetadata(String sent, String otid, String column) {

        logger.info("updateMetadata in Dao");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

//            String updateOriginal = "update uois set is_latest_version = 'N' where uoi_id = :objectId";
            String updateOriginal = "update NICK_PROD_ASSET_METADATA set "+column+"=:sent where uoi_id = :objectId";
            session.createNativeQuery(updateOriginal).setParameter("sent", sent).setParameter("objectId", otid).executeUpdate();

//            String updateLatest = "update uois set logical_uoi_id=:originalAsset, version=2, is_latest_version = 'Y' where uoi_id = :updatedAsset";
            String updateLatest = "Insert into INDEX_WORKQUEUE Values (INDEX_WORKQUEUE_ID_SEQ.nextval, :objectId, 'UPDATE',113, 'NONE',sysdate)";
            session.createNativeQuery(updateLatest).setParameter("objectId", otid).executeUpdate();


            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            logger.error("Error in updateUoisNew   {} ",e.getMessage());

        }

 /*       public void addToIndexer(String uoiId) {
            logger.info("addToIndexer");
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
                logger.error("Error in addToIndexer   {} ",e.getMessage());

            }
        }*/
    }

    @Override
    public void updateMetadata2(String sent, String otid, String column) {
        logger.info("updateMetadata2 in Dao");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

//            String updateOriginal = "update uois set is_latest_version = 'N' where uoi_id = :objectId";
            String updateOriginal = "update NICK_PROD_ASSET_METATDATA2 set "+column+"=:sent where uoi_id = :objectId";
            session.createNativeQuery(updateOriginal).setParameter("sent", sent).setParameter("objectId", otid).executeUpdate();

//            String updateLatest = "update uois set logical_uoi_id=:originalAsset, version=2, is_latest_version = 'Y' where uoi_id = :updatedAsset";
            String updateLatest = "Insert into INDEX_WORKQUEUE Values (INDEX_WORKQUEUE_ID_SEQ.nextval, :objectId, 'UPDATE',113, 'NONE',sysdate)";
            session.createNativeQuery(updateLatest).setParameter("objectId", otid).executeUpdate();


            transaction.commit();
        } catch (Exception e) {
            if(transaction != null){
                transaction.rollback();
            }
            logger.error("Error in updateMetadata2   {} ",e.getMessage());

        }

    }

    @Override
    public NickProdMetadataUpdateMarch25 getMetadataFromDump(String otid) {

        NickProdMetadataUpdateMarch25 nickProdMetadataUpdateMarch25 = null;

        try (Session session = sessionFactory.openSession()){


            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<NickProdMetadataUpdateMarch25> criteria = builder.createQuery(NickProdMetadataUpdateMarch25.class);
            Root<NickProdMetadataUpdateMarch25> root = criteria.from(NickProdMetadataUpdateMarch25.class);

            Predicate fromOtid = builder.equal(root.get("otid"),otid);
            criteria.select(root).where(fromOtid);

            criteria.orderBy(builder.asc(root.get("id")));

            Query<NickProdMetadataUpdateMarch25> squery = session.createQuery(criteria);

            nickProdMetadataUpdateMarch25 = squery.getSingleResult();

        }catch(Exception e){
            logger.info("Error in getMetadataFromDump   {} ",e);
            //throw new RuntimeException(e.getMessage());
        }

        return nickProdMetadataUpdateMarch25;
    }


}
