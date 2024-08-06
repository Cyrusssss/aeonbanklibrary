package com.aeonbank.library.cron;

import com.aeonbank.library.repository.TransactionArchiveRepository;
import com.aeonbank.library.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Component
public class TransactionHousekeep {

    @Autowired
    private TransactionRepository transRepository;

    @Autowired
    private TransactionArchiveRepository transArchiveRepository;

    // every X minutes
    @Transactional
    @Scheduled(cron = "0 */1 * * * ?")
    public void housekeep() {
        log.info("[housekeep]cron job started");
        boolean success = false;
        tryBlock: try {
            // select id from transaction where is_returned = 1 for update;
            List<Long> idList = transRepository.getReturnedId();
            if (idList.isEmpty()) {
                log.info("[housekeep]nothing to housekeep, job ignored");
                success = true;
                break tryBlock;
            }

            // insert into transaction_archive (select * from transaction where id in ?)
            int affectedCount = transArchiveRepository.insertTrans(idList);
            if (affectedCount != idList.size()) {
                log.error("[housekeep]insertedCount not tally with idList. insertedCount:{} idList.size:{}", affectedCount, idList.size());
                break tryBlock;
            }

            // delete from transaction where id in ?
            affectedCount = transRepository.deleteByIds(idList);
            if (affectedCount != idList.size()) {
                log.error("[housekeep]deletedCount not tally with idList. deletedCount:{} idList.size:{}", affectedCount, idList.size());
                break tryBlock;
            }

            success = true;
            log.info("[housekeep]cron job executed successfully");
        } catch (Exception e) {
            log.error("[housekeep]something went wrong. error >>> ", e);
        }
        if (!success) {
            log.error("[housekeep]rolling back transaction");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        log.info("[housekeep]end of cron job");
    }

}
