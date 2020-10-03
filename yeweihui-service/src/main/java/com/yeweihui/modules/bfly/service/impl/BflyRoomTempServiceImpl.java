package com.yeweihui.modules.bfly.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.annotation.BeanFieldIndex;
import com.yeweihui.modules.bfly.dao.BflyRoomDao;
import com.yeweihui.modules.bfly.dao.BflyRoomTempDao;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import com.yeweihui.modules.bfly.entity.BflyRoomTemp;
import com.yeweihui.modules.bfly.service.BflyRoomTempService;
import com.yeweihui.modules.user.dao.ZonesDao;
import com.yeweihui.modules.user.entity.ZonesEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class BflyRoomTempServiceImpl extends ServiceImpl<BflyRoomTempDao, BflyRoomTemp> implements BflyRoomTempService {

    @Resource
    private BflyRoomDao bflyRoomDao;

    @Resource
    private ZonesDao zonesDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAll(Long zoneId, Long userId, List<Long> tempIds) {
        List<BflyRoomTemp> roomTemps = this.baseMapper.selectList(new EntityWrapper<BflyRoomTemp>()
                .eq("operation_uid", userId).eq("zone_id", zoneId).in("id",tempIds));
        for (BflyRoomTemp roomTemp : roomTemps) {
            BflyRoom room = new BflyRoom(roomTemp);
            bflyRoomDao.insert(room);
        }
        this.baseMapper.delete(new EntityWrapper<BflyRoomTemp>()
                .eq("operation_uid", userId).eq("zone_id", zoneId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BflyRoomTemp> importExcel(Long userId, Long zoneId, InputStream inputStream) throws Exception {
        ZonesEntity zonesEntity = zonesDao.selectById(zoneId);
        if (null != zonesEntity) {
            List<BflyRoomTemp> bflyRoomTemps = parseExcel(userId, zoneId, zonesEntity.getName(), inputStream);
            bflyRoomTemps.forEach(bflyRoomTemp -> this.baseMapper.insert(bflyRoomTemp));
            return bflyRoomTemps;
        }
        return null;
    }

    private List<BflyRoomTemp> parseExcel(Long userId, Long zoneId, String zoneName, InputStream inputStream) throws Exception {
        List<BflyRoomTemp> bflyRoomTemps = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        boolean isFirst = true;
        boolean isBreak = false;
        for (Row row : sheet) {
            if (isFirst) {
                isFirst = false;
                continue;
            }
            if (0 != row.getFirstCellNum()) {
                break;
            }
            BflyRoomTemp bflyRoomTemp = new BflyRoomTemp(userId, zoneId);
            Field[] fields = bflyRoomTemp.getClass().getDeclaredFields();
            for (int index=0; index<=row.getLastCellNum(); index++) {
                String value = "";
                Cell cell = row.getCell(index);
                if (null != cell) {
                    switch (cell.getCellType()) {
                        case STRING:
                            value = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            DecimalFormat df = new DecimalFormat("0.##");
                            value = df.format(cell.getNumericCellValue());
                            break;
                    }
                    value = value.trim();
                }
                if (0 == index) {
                    if (StringUtils.isBlank(value)) {
                        isBreak = true;
                        break;
                    }
                    if (!value.equals(zoneName)) {
                        throw new Exception("小区名不一致!");
                    }
                }
                for (Field field : fields) {
                    BeanFieldIndex beanFieldIndex = field.getAnnotation(BeanFieldIndex.class);
                    if (null != beanFieldIndex && index == beanFieldIndex.index()) {
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), bflyRoomTemp.getClass());
                        Method writeMethod = pd.getWriteMethod();
                        if (null != writeMethod) {
                            try {
                                Parameter[] parameters = writeMethod.getParameters();
                                if (null != parameters && parameters.length > 0) {
                                    if (parameters[0].getType() == BigDecimal.class) {
                                        writeMethod.invoke(bflyRoomTemp, new BigDecimal(value));
                                    } else if (parameters[0].getType() == String.class) {
                                        writeMethod.invoke(bflyRoomTemp, value);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                };
            }
            if (isBreak) {
                break;
            }
            bflyRoomTemps.add(bflyRoomTemp);
        }
        return bflyRoomTemps;
    }
}
