package pl.rawinet.detal.service.Interfaces;

import pl.rawinet.detal.model.Notice;

import java.util.List;

public interface NoticeService {
    public List<Notice> getAll();
    public List<Notice> getAllCustomersNotices(Integer id);
    public void deleteAllCustomersNotices(Integer id);
    public Notice getNoticeByCustomerId(Integer id);
    public void saveOrUpdate(Notice notice);
}
