package org.ruoyi.utils;

import cn.hutool.core.collection.CollUtil;
import org.ruoyi.common.core.utils.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * CVSS评分计算工具类
 * 基于CVSS 4.0标准计算风险评分
 *
 * @author system
 * @date 2026-01-29
 */
public class CvssScoreCalculator {

    //CVSS维度分值映射
    private static final double AV_N = 0.85;
    private static final double AV_A = 0.62;
    private static final double AV_L = 0.55;
    private static final double AV_P = 0.2;

    private static final double AC_L = 0.77;
    private static final double AC_H = 0.44;

    private static final double PR_N = 0.85;
    private static final double PR_L = 0.62;
    private static final double PR_H = 0.27;

    private static final double UI_N = 0.85;
    private static final double UI_R = 0.62;

    private static final double IMPACT_C = 0.22;
    private static final double IMPACT_I = 0.22;
    private static final double IMPACT_A = 0.22;

    //归一化系数
    private static final double NORMALIZATION_FACTOR = 1.08;

    /**
     * 计算CVSS基础评分
     *
     * @param attackVector 攻击方式：N=远程、A=网络相邻、L=本地、P=物理
     * @param attackComplexity 利用复杂度：L=低、H=高
     * @param privilegesRequired 权限需求：N=无需权限、L=需要权限、H=高级权限
     * @param userInteraction 用户交互：N=无需交互、R=需要交互
     * @param impact 影响范围数组：C=机密性、I=完整性、A=可用性
     * @return CVSS评分（0-10），如果维度不完整返回null
     */
    public static BigDecimal calculateCvssScore(String attackVector, String attackComplexity,
                                                  String privilegesRequired, String userInteraction,
                                                  List<String> impact) {
        if (StringUtils.isBlank(attackVector) || StringUtils.isBlank(attackComplexity)
            || StringUtils.isBlank(privilegesRequired) || StringUtils.isBlank(userInteraction)
            || CollUtil.isEmpty(impact)) {
            return null;
        }

        double baseScore = 0;

        //攻击方式分值
        switch (attackVector.toUpperCase()) {
            case "N":
                baseScore += AV_N;
                break;
            case "A":
                baseScore += AV_A;
                break;
            case "L":
                baseScore += AV_L;
                break;
            case "P":
                baseScore += AV_P;
                break;
            default:
                return null;
        }

        //利用复杂度分值
        switch (attackComplexity.toUpperCase()) {
            case "L":
                baseScore += AC_L;
                break;
            case "H":
                baseScore += AC_H;
                break;
            default:
                return null;
        }

        //权限需求分值
        switch (privilegesRequired.toUpperCase()) {
            case "N":
                baseScore += PR_N;
                break;
            case "L":
                baseScore += PR_L;
                break;
            case "H":
                baseScore += PR_H;
                break;
            default:
                return null;
        }

        //用户交互分值
        switch (userInteraction.toUpperCase()) {
            case "N":
                baseScore += UI_N;
                break;
            case "R":
                baseScore += UI_R;
                break;
            default:
                return null;
        }

        //影响范围分值（取最大值乘以3）
        double maxImpact = 0;
        for (String imp : impact) {
            switch (imp.toUpperCase()) {
                case "C":
                    maxImpact = Math.max(maxImpact, IMPACT_C);
                    break;
                case "I":
                    maxImpact = Math.max(maxImpact, IMPACT_I);
                    break;
                case "A":
                    maxImpact = Math.max(maxImpact, IMPACT_A);
                    break;
            }
        }
        baseScore += maxImpact * 3;

        //归一化到0-10
        double score = Math.min(10, Math.max(0, baseScore * NORMALIZATION_FACTOR));

        //转换为BigDecimal并保留一位小数
        return BigDecimal.valueOf(score).setScale(1, RoundingMode.HALF_UP);
    }

    /**
     * 根据CVSS评分映射风险等级
     *
     * @param cvssScore CVSS评分（0-10）
     * @return 风险等级：none/low/medium/high/critical
     */
    public static String mapSeverityByScore(BigDecimal cvssScore) {
        if (cvssScore == null) {
            return null;
        }

        double scoreValue = cvssScore.doubleValue();
        if (scoreValue >= 9.0) {
            return "critical";
        } else if (scoreValue >= 7.0) {
            return "high";
        } else if (scoreValue >= 4.0) {
            return "medium";
        } else if (scoreValue >= 0.1) {
            return "low";
        } else {
            return "none";
        }
    }
}
