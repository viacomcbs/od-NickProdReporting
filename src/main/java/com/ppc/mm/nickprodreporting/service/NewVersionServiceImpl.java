package com.ppc.mm.nickprodreporting.service;

import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodreporting.dao.NewVersionDAO;
import com.ppc.mm.nickprodreporting.dao.NewVersionDAOImpl;
import com.ppc.mm.nickprodreporting.entity.NpNewVersionReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewVersionServiceImpl implements NewVersionService {

    NewVersionDAO versionDAO;

    @Autowired
    public NewVersionServiceImpl(NewVersionDAOImpl newVersionDAO) {
        this.versionDAO = newVersionDAO;
    }

    @Override
    public List<NpNewVersionReport> getNewVersionReport() {
        return versionDAO.getNewVersionReports();
    }

    @Override
    public String getUoiId(NpNewVersionReport mmInboundReport) {

        return versionDAO.getOldUoiId(mmInboundReport.getLogicalUoiId(),mmInboundReport.getVersion());
    }

    @Override
    public NickProdAssetMetadata getAssetMetadata(String uoiId) {
        return versionDAO.getAssetMetadata(uoiId);
    }

    @Override
    public void saveNewVersionReport(NpNewVersionReport mmInboundReport) {
        versionDAO.saveNewVersionReport(mmInboundReport);
    }

    @Override
    public List<String> getFileNamesForProcess(Integer prodId) {
        return versionDAO.getFileNamesForProcess(prodId);
    }

    @Override
    public void executeProcedure(List<String> nickObjects) {
        versionDAO.executeProcedure(nickObjects);
    }

    @Override
    public List<String> getUoiIds(String nickObject) {
        return versionDAO.getUoiIds(nickObject);
    }

    @Override
    public void updateUois(String id, int counter, String isLatest, String original) {
        versionDAO.updateUois( id,  counter, isLatest, original);
    }

    @Override
    public void insertFileCompareObjects(String id) {
        versionDAO.insertFileCompareObjects( id);
    }

    @Override
    public void updateFileNameObjects(String nickObject) {
        versionDAO.updateFileNameObjects(nickObject);
    }
}
