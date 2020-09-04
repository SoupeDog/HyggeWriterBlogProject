package org.xavier.blog.service;

import org.springframework.util.LinkedMultiValueMap;
import org.xavier.blog.domain.po.AccessRule;
import org.xavier.blog.domain.po.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ArticleService {
    default boolean checkAccessRule(User loginUser, AccessRule accessRule, HashMap<String, Object> allJoinedGroup, String secretKey) {
        switch (accessRule.getAccessPermit()) {
            case PUBLIC:
                return true;
            case PERSONAL:
                return loginUser.getUid().equals(accessRule.getExtendProperties());
            case SECRET_KEY:
                return accessRule.getExtendProperties().equals(secretKey);
            case GROUP:
                return allJoinedGroup.containsKey(accessRule.getExtendProperties());
            default:
                return false;
        }
    }

    default boolean checkAccessRuleOfOneArticleCategoryNo(String secretKey, User loginUser, List<AccessRule> accessRuleList, HashMap<String, Object> allJoinedGroup) {
        boolean pass = false;
        for (AccessRule accessRule : accessRuleList) {
            if (accessRule.getRequirement()) {
                pass = pass && checkAccessRule(loginUser, accessRule, allJoinedGroup, secretKey);
                if (!pass) {
                    break;
                }
            } else {
                pass = checkAccessRule(loginUser, accessRule, allJoinedGroup, secretKey) || pass;
            }
        }
        return pass;
    }

    default ArrayList<String> getAllowableArticleCategoryNoList(String secretKey, User loginUser, HashMap<String, Object> allJoinedGroup, ArrayList<AccessRule> accessRuleList) {
        // key-value ArticleCategoryNo-AccessRule
        LinkedMultiValueMap<String, AccessRule> accessRuleOrderMap = new LinkedMultiValueMap<>();
        for (AccessRule accessRule : accessRuleList) {
            accessRuleOrderMap.add(accessRule.getArticleCategoryNo(), accessRule);
        }
        ArrayList<String> allowableArticleCategoryNoList = new ArrayList<>();
        for (Map.Entry<String, List<AccessRule>> entry : accessRuleOrderMap.entrySet()) {
            if (checkAccessRuleOfOneArticleCategoryNo(secretKey, loginUser, entry.getValue(), allJoinedGroup)) {
                allowableArticleCategoryNoList.add(entry.getKey());
            }
        }
        return allowableArticleCategoryNoList;
    }
}