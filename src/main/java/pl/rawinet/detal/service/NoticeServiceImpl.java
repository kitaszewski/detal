package pl.rawinet.detal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rawinet.detal.dao.NoticeDao;
import pl.rawinet.detal.model.Notice;
import pl.rawinet.detal.service.Interfaces.NoticeService;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeDao noticeDao;

    @Override
    public List<Notice> getAll() {
        return (List<Notice>) noticeDao.findAll();
    }

    @Override
    public List<Notice> getAllCustomersNotices(Integer id) {
        if(noticeDao.existsNoticeByCustomerId(id)){
            return (List<Notice>) noticeDao.findAllByCustomerIdOrderByIdDesc(id);
        }
        return null;
    }

    @Override
    public void deleteAllCustomersNotices(Integer id) {
        if(noticeDao.existsNoticeByCustomerId(id)){
            noticeDao.findAllByCustomerIdOrderByIdDesc(id).forEach(e -> noticeDao.delete(e));
        }
    }

    @Override
    public Notice getNoticeByCustomerId(Integer id) {
        return null;
    }

    @Override
    public void saveOrUpdate(Notice notice) {
        noticeDao.save(notice);
    }
}
