package net.torosamy.torosamyCore.holder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.torosamy.torosamyCore.TorosamyCore;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeHolder extends PlaceholderExpansion {

    private final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private final SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
    private final SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
    private final SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
    private final SimpleDateFormat secondFormat = new SimpleDateFormat("ss");
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.CHINA);
    private final SimpleDateFormat shortDayFormat = new SimpleDateFormat("E", Locale.CHINA);

    @Override
    public @NotNull String getIdentifier() {
        return "timeoperate";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Torosamy";
    }

    @Override
    public @NotNull String getVersion() {
        return TorosamyCore.plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();

        // 基础时间单位
        if ("year".equals(params)) {
            return yearFormat.format(now);
        }

        if ("month".equals(params)) {
            return monthFormat.format(now);
        }

        if ("date".equals(params)) {
            // 几号
            return dateFormat.format(now);
        }

        if ("day".equals(params)) {
            // 星期几（完整名称）
            return dayFormat.format(now);
        }

        if ("day_short".equals(params)) {
            // 星期几（简写）
            return shortDayFormat.format(now);
        }
        
        if ("day_number".equals(params)) {
            // 星期几的数字表示（1-7，1=周日，7=周六）
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return String.valueOf(dayOfWeek);
        }

        if ("hour".equals(params)) {
            return hourFormat.format(now);
        }

        if ("hour_12".equals(params)) {
            // 12小时制
            SimpleDateFormat hour12Format = new SimpleDateFormat("hh");
            return hour12Format.format(now);
        }

        if ("minute".equals(params)) {
            return minuteFormat.format(now);
        }

        if ("second".equals(params)) {
            return secondFormat.format(now);
        }

        if ("ampm".equals(params)) {
            // 上午/下午
            SimpleDateFormat ampmFormat = new SimpleDateFormat("a", Locale.CHINA);
            return ampmFormat.format(now);
        }

        // 组合格式
        if ("full".equals(params)) {
            // 完整日期时间
            SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return fullFormat.format(now);
        }

        if ("full_cn".equals(params)) {
            // 中文格式完整日期时间
            SimpleDateFormat fullCnFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
            return fullCnFormat.format(now);
        }

        if ("date_cn".equals(params)) {
            // 中文日期
            SimpleDateFormat dateCnFormat = new SimpleDateFormat("yyyy年MM月dd日");
            return dateCnFormat.format(now);
        }

        if ("time_cn".equals(params)) {
            // 中文时间
            SimpleDateFormat timeCnFormat = new SimpleDateFormat("HH时mm分ss秒");
            return timeCnFormat.format(now);
        }

        if ("datetime".equals(params)) {
            // 标准日期时间
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return datetimeFormat.format(now);
        }

        // 月份相关
        if ("month_cn".equals(params)) {
            // 中文月份
            int month = calendar.get(Calendar.MONTH) + 1;
            return month + "月";
        }

        if ("month_name".equals(params)) {
            // 月份英文名称
            SimpleDateFormat monthNameFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
            return monthNameFormat.format(now);
        }

        if ("month_name_short".equals(params)) {
            // 月份英文简写
            SimpleDateFormat monthShortFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
            return monthShortFormat.format(now);
        }

        // 季度
        if ("quarter".equals(params)) {
            // 季度（1-4）
            int month = calendar.get(Calendar.MONTH) + 1;
            int quarter = (month - 1) / 3 + 1;
            return String.valueOf(quarter);
        }

        // 一年中的第几天
        if ("day_of_year".equals(params)) {
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            return String.valueOf(dayOfYear);
        }

        // 一周中的第几天（国际标准，1=周一，7=周日）
        if ("iso_day".equals(params)) {
            int isoDay = calendar.get(Calendar.DAY_OF_WEEK);
            // 转换国际标准（周一=1，周日=7）
            if (isoDay == 1) return "7"; // 周日
            return String.valueOf(isoDay - 1);
        }

        // 时间戳
        if ("timestamp".equals(params)) {
            return String.valueOf(System.currentTimeMillis());
        }

        if ("timestamp_seconds".equals(params)) {
            return String.valueOf(System.currentTimeMillis() / 1000);
        }

        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return onRequest(player, params);
    }
}