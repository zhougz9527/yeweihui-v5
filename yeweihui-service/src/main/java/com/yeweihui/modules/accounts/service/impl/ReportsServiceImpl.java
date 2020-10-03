package com.yeweihui.modules.accounts.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.modules.accounts.dao.AccountsFinancialinformDao;
import com.yeweihui.modules.accounts.entity.AccountsEntity;
import com.yeweihui.modules.accounts.entity.AccountsFinancialinformEntity;
import com.yeweihui.modules.accounts.service.AccountsFinancialinformService;
import com.yeweihui.modules.accounts.service.AccountsService;
import com.yeweihui.modules.accounts.service.ReportsService;
import com.yeweihui.modules.vo.api.vo.FileInfoVO;
import com.yeweihui.modules.vo.query.AccountsQueryParam;
import com.yeweihui.modules.vo.query.ReceiptsAndDisbursementsParam;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zss86 on 2020/8/21.
 */
@Service("ReportsService")
public class ReportsServiceImpl extends ServiceImpl<AccountsFinancialinformDao, AccountsFinancialinformEntity> implements ReportsService {

    @Autowired
    AccountsService accountsService;
    @Autowired
    AccountsFinancialinformService accountsFinancialinformService;


    public Map monthOrYearStatistical(Long zoneId, Date statisticalDate, boolean isYear) {
        Map map = new HashMap<String, Object>();
        Map reportsmap = new HashMap<String, Object>();
        AccountsQueryParam accountsQueryParam = new AccountsQueryParam();
        accountsQueryParam.setZoneId(zoneId);
        accountsQueryParam.getStatus().add(3);
        if (!isYear) {
            if (statisticalDate != null) {
                accountsQueryParam.setAccountsDate(statisticalDate);

            }
        }
        else
        {
            if (statisticalDate != null) {
                Calendar calendary = Calendar.getInstance();
                calendary.setTime(statisticalDate);
                accountsQueryParam.setYearDate(calendary.get(Calendar.YEAR));
            }
        }
        List<AccountsEntity> list = accountsService.simpleList(accountsQueryParam);
        if (list != null && list.size() > 0) {
            Calendar calendar = Calendar.getInstance();

            AccountsEntity accountsEntity = list.get(0);
            ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam = new ReceiptsAndDisbursementsParam();
            receiptsAndDisbursementsParam.setZoneId(zoneId);
            receiptsAndDisbursementsParam.setStatus(3);
            if (isYear) {
                if (statisticalDate != null) {
                    calendar.setTime(statisticalDate);
                } else {
                    calendar.setTime(accountsEntity.getStartDate());
                }
                receiptsAndDisbursementsParam.setYearDate(calendar.get(Calendar.YEAR));
                map.put("statisticalDate", calendar.get(Calendar.YEAR));

            } else {
                receiptsAndDisbursementsParam.setAccountsDate(accountsEntity.getEndDate());
                map.put("statisticalDate", accountsEntity.getEndDate());

            }

            receiptsAndDisbursementsParam.setRdtype(2); //收支类型：1(收入)、2(支出),收支(receipts and disbursements)
            receiptsAndDisbursementsParam.setType(1); //科目类型：1(经营)、2(押金)

            //本月经营支出
            reportsmap.put("operationsDisbursements", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));
            map.put("operations_disbursements_charts", this.baseMapper.getReceiptsAndDisbursementsBYSubject(receiptsAndDisbursementsParam));

            receiptsAndDisbursementsParam.setRdtype(1);
            receiptsAndDisbursementsParam.setType(1);
            //本月经营收入
            reportsmap.put("operationsReceipts", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));
            map.put("operations_receipts_charts", this.baseMapper.getReceiptsAndDisbursementsBYSubject(receiptsAndDisbursementsParam));


            receiptsAndDisbursementsParam.setRdtype(1);
            receiptsAndDisbursementsParam.setType(2);

            //本月押金收入
            reportsmap.put("depositReceipts", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));
            map.put("deposit_receipts_charts", this.baseMapper.getReceiptsAndDisbursementsBYSubject(receiptsAndDisbursementsParam));

            receiptsAndDisbursementsParam.setRdtype(2);
            receiptsAndDisbursementsParam.setType(2);
            //本月押金支出
            reportsmap.put("depositDisbursements", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));
            map.put("deposit_disbursements_charts", this.baseMapper.getReceiptsAndDisbursementsBYSubject(receiptsAndDisbursementsParam));


            //本月经营结余
            receiptsAndDisbursementsParam.setType(1);
            reportsmap.put("operationsBlance", this.baseMapper.getSurplusByType(receiptsAndDisbursementsParam));

            //本月押金结余
            receiptsAndDisbursementsParam.setType(2);
            reportsmap.put("depositBlance", this.baseMapper.getSurplusByType(receiptsAndDisbursementsParam));


            if (isYear) {
                Date date=null;
                if (statisticalDate != null)//传入时间不为空获取前一年信息
                {
                    date=getYearStart(statisticalDate);
                } else {
                    date=getYearStart(accountsEntity.getStartDate());

                }
                AccountsQueryParam accountsQueryParamy = new AccountsQueryParam();
                accountsQueryParamy.setZoneId(zoneId);
                accountsQueryParamy.getStatus().add(3);
                accountsQueryParamy.setAccountsDate(date);
                List<AccountsEntity> listy = accountsService.simpleList(accountsQueryParamy);
                if (listy != null && listy.size() > 0) {
                    reportsmap.put("operationsBlanceL", listy.get(0).getLastOperatingSurplus());
                    reportsmap.put("depositBlanceL", listy.get(0).getLastPledgeSurplus());
                } else {
                    reportsmap.put("operationsBlanceL", "");
                    reportsmap.put("depositBlanceL", "");

                }
            } else {
                //上月经营结余
                reportsmap.put("operationsBlanceL", accountsEntity.getLastOperatingSurplus());
                //上月押金结余
                reportsmap.put("depositBlanceL", accountsEntity.getLastPledgeSurplus());
            }
            map.put("isNull", 0);


        } else {
            map.put("isNull", 1);
            reportsmap.put("operationsDisbursements", "");
            map.put("operations_disbursements_charts", "");


            reportsmap.put("operationsReceipts", "");
            map.put("operations_receipts_charts", "");



            //本月押金收入
            reportsmap.put("depositReceipts","");
            map.put("deposit_receipts_charts","");


            //本月押金支出
            reportsmap.put("depositDisbursements", "");
            map.put("deposit_disbursements_charts","");


            //本月经营结余
            reportsmap.put("operationsBlance","");

            //本月押金结余
            reportsmap.put("depositBlance", "");
            //上月经营结余
            reportsmap.put("operationsBlanceL", "");
            //上月押金结余
            reportsmap.put("depositBlanceL", "");

            map.put("statisticalDate", statisticalDate);


        }
        map.put("reports", reportsmap);
        return map;
    }


    public Map blance(Long zoneId) {
        AccountsQueryParam accountsQueryParam = new AccountsQueryParam();
        accountsQueryParam.setZoneId(zoneId);
        double surplus = 0;
        Integer dredge = 0;
        Date endDate = null;
        List<AccountsEntity> list = accountsService.simpleList(accountsQueryParam);
        if (list != null && list.size() > 0) {

            AccountsEntity accountsEntity = list.get(0);
            if(list.size()==1&&accountsEntity.getStatus()<=3)
            {
                dredge = 1;
            }
            else {
                ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam = new ReceiptsAndDisbursementsParam();
                receiptsAndDisbursementsParam.setZoneId(zoneId);
                receiptsAndDisbursementsParam.setStatus(3);
                AccountsEntity accountsEntity3 = list.get(1);
                receiptsAndDisbursementsParam.setAccountsDate(accountsEntity3.getEndDate());
                String s = this.baseMapper.getSurplusByType(receiptsAndDisbursementsParam);
                if ("" != s && null != s) {
                    surplus = Double.parseDouble(s);
                    dredge = 2;
                    endDate = accountsEntity.getEndDate();
                }
            }
        }
        Map map = new HashMap<String, Object>();
        map.put("blance", surplus);
        map.put("dredge", dredge);
        map.put("statisticalDate", endDate);
        return map;
    }

    public Map detail(Long zoneId, Date statisticalDate, boolean isYear) {
        Map map = new HashMap<String, Object>();
        Map reportsmap = new HashMap<String, Object>();
        AccountsQueryParam accountsQueryParam = new AccountsQueryParam();
        accountsQueryParam.setZoneId(zoneId);
        accountsQueryParam.getStatus().add(3);
        if (!isYear) {
            if (statisticalDate != null) {
                accountsQueryParam.setAccountsDate(statisticalDate);
            }

        }
        else
        {
            if (statisticalDate != null) {
                Calendar calendary = Calendar.getInstance();
                calendary.setTime(statisticalDate);
                accountsQueryParam.setYearDate(calendary.get(Calendar.YEAR));
            }
        }
        List<AccountsEntity> list = accountsService.simpleList(accountsQueryParam);
        if (list != null && list.size() > 0) {
            AccountsEntity accountsEntity = list.get(0);
            Calendar calendar = Calendar.getInstance();
            ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam = new ReceiptsAndDisbursementsParam();
            receiptsAndDisbursementsParam.setZoneId(zoneId);
            receiptsAndDisbursementsParam.setStatus(3);
            if (isYear) {

                calendar.setTime(statisticalDate);
                receiptsAndDisbursementsParam.setYearDate(calendar.get(Calendar.YEAR));
                map.put("statisticalDate", calendar.get(Calendar.YEAR));

            } else {
                receiptsAndDisbursementsParam.setAccountsDate(statisticalDate);
                map.put("statisticalDate", statisticalDate);

            }
            //收入详情
            receiptsAndDisbursementsParam.setRdtype(1);
            map.put("receiptsDetail", this.baseMapper.getReceiptsAndDisbursementsBYSubject(receiptsAndDisbursementsParam));
            //支出详情
            receiptsAndDisbursementsParam.setRdtype(2);
            map.put("disbursementsDetail", this.baseMapper.getReceiptsAndDisbursementsBYSubject(receiptsAndDisbursementsParam));

            receiptsAndDisbursementsParam.setRdtype(2); //收支类型：1(收入)、2(支出),收支(receipts and disbursements)
            receiptsAndDisbursementsParam.setType(1); //科目类型：1(经营)、2(押金)

            //本月经营支出
            reportsmap.put("operationsDisbursements", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));

            receiptsAndDisbursementsParam.setRdtype(1);
            receiptsAndDisbursementsParam.setType(1);
            //本月经营收入
            reportsmap.put("operationsReceipts", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));


            receiptsAndDisbursementsParam.setRdtype(1);
            receiptsAndDisbursementsParam.setType(2);

            //本月押金收入
            reportsmap.put("depositReceipts", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));

            receiptsAndDisbursementsParam.setRdtype(2);
            receiptsAndDisbursementsParam.setType(2);
            //本月押金支出
            reportsmap.put("depositDisbursements", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));


            //本月经营结余
            receiptsAndDisbursementsParam.setType(1);
            reportsmap.put("operationsBlance", this.baseMapper.getSurplusByType(receiptsAndDisbursementsParam));

            //本月押金结余
            receiptsAndDisbursementsParam.setType(2);
            reportsmap.put("depositBlance", this.baseMapper.getSurplusByType(receiptsAndDisbursementsParam));


            if (isYear) {
                Date date=null;
                if (statisticalDate != null)//传入时间不为空获取前一年信息
                {
                    date=getYearStart(statisticalDate);
                } else {
                    date=getYearStart(accountsEntity.getStartDate());

                }
                AccountsQueryParam accountsQueryParamy = new AccountsQueryParam();
                accountsQueryParamy.setZoneId(zoneId);
                accountsQueryParamy.getStatus().add(3);
                accountsQueryParamy.setAccountsDate(date);
                List<AccountsEntity> listy = accountsService.simpleList(accountsQueryParamy);
                if (listy != null && listy.size() > 0) {
                    reportsmap.put("operationsBlanceL", listy.get(0).getLastOperatingSurplus());
                    reportsmap.put("depositBlanceL", listy.get(0).getLastPledgeSurplus());
                }else {
                    reportsmap.put("operationsBlanceL", 0);
                    reportsmap.put("depositBlanceL", 0);

                }
            } else {
                //上月经营结余
                reportsmap.put("operationsBlanceL", accountsEntity.getLastOperatingSurplus());
                //上月押金结余
                reportsmap.put("depositBlanceL", accountsEntity.getLastPledgeSurplus());
            }
            map.put("isNull", 0);
        } else {
            map.put("isNull", 1);
            reportsmap.put("operationsDisbursements", "");


            reportsmap.put("operationsReceipts", "");



            //本月押金收入
            reportsmap.put("depositReceipts","");


            //本月押金支出
            reportsmap.put("depositDisbursements", "");


            //本月经营结余
            reportsmap.put("operationsBlance","");

            //本月押金结余
            reportsmap.put("depositBlance", "");
            //上月经营结余
            reportsmap.put("operationsBlanceL", "");
            //上月押金结余
            reportsmap.put("depositBlanceL", "");

            map.put("receiptsDetail",null);
            //支出详情
            map.put("disbursementsDetail",null);


            map.put("statisticalDate", statisticalDate);



        }
        map.put("reports", reportsmap);
        return map;
    }

    public Map selectSubjectDetail(ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam) {
        Map map = new HashMap<String, Object>();

        map.put("reports", accountsFinancialinformService.getFinancialinformVODetail(this.baseMapper.selectSubjectDetail(receiptsAndDisbursementsParam)));

        return map;
    }

    public Map recorded(Long zoneId, Date statisticalDate) {
        if(this.equals(statisticalDate,new Date()))
        {
            AccountsQueryParam accountsQueryParam = new AccountsQueryParam();
            accountsQueryParam.setZoneId(zoneId);
            List<AccountsEntity> list = accountsService.simpleList(accountsQueryParam);
            if(list!=null&&list.size()>0)
            statisticalDate=list.get(0).getStartDate();
        }
        Map reportsmap = new HashMap<String, Object>();
        ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam = new ReceiptsAndDisbursementsParam();
        receiptsAndDisbursementsParam.setZoneId(zoneId);
        receiptsAndDisbursementsParam.setAccountsDate(statisticalDate);
        receiptsAndDisbursementsParam.setRdtype(2); //收支类型：1(收入)、2(支出),收支(receipts and disbursements)
        receiptsAndDisbursementsParam.setType(1); //科目类型：1(经营)、2(押金)
        //本月经营支出
        reportsmap.put("operationsDisbursements", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));

        receiptsAndDisbursementsParam.setRdtype(1);
        receiptsAndDisbursementsParam.setType(1);
        //本月经营收入
        reportsmap.put("operationsReceipts", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));


        receiptsAndDisbursementsParam.setRdtype(1);
        receiptsAndDisbursementsParam.setType(2);

        //本月押金收入
        reportsmap.put("depositReceipts", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));

        receiptsAndDisbursementsParam.setRdtype(2);
        receiptsAndDisbursementsParam.setType(2);
        //本月押金支出
        reportsmap.put("depositDisbursements", this.baseMapper.getReceiptsAndDisbursements(receiptsAndDisbursementsParam));


        //本月经营结余
        receiptsAndDisbursementsParam.setType(1);
        reportsmap.put("operationsBlance", this.baseMapper.getSurplusByType(receiptsAndDisbursementsParam));

        //本月押金结余
        receiptsAndDisbursementsParam.setType(2);
        reportsmap.put("depositBlance", this.baseMapper.getSurplusByType(receiptsAndDisbursementsParam));


        return reportsmap;

    }

    public Map getFileInfoVO(Date statisticalDate) {
        Map map = new HashMap<String, Object>();
        AccountsEntity accountsEntity = accountsService.getAccountsByMonth(statisticalDate);
        if (accountsEntity != null) {
            FileInfoVO fileInfoVO = accountsService.getStatementOfAccounts(accountsEntity.getId());
            map.put("fileInfoVO", fileInfoVO);
        }
        return map;
    }


    public static Date getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static java.util.Date getEndDayOfYear(Integer year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }
    public static Date getYearStart(Date date){
        String sdate=new SimpleDateFormat("yyyy").format(date)+"-01-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean equals(Date date1, Date date2) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);

    }

}
