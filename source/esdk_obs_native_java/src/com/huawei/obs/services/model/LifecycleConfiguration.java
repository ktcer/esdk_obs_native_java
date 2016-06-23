/**
* Copyright 2015 Huawei Technologies Co., Ltd. All rights reserved.
* eSDK is licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.huawei.obs.services.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述桶的生命周期配置
 */
public class LifecycleConfiguration
{
    public static final String STORAGE_CLASS_GLACIER = "GLACIER";

    // 具体某一条生命周期配置的Container
    private List<Rule> rules = new ArrayList<Rule>();

    /**
     * 桶的生命周期的构造方法
     * @param rules 桶的生命周期的规则集合
     */
    public LifecycleConfiguration(List<Rule> rules)
    {
        this.rules = rules;
    }

    /**
     * 桶的生命周期的构造方法
     */
    public LifecycleConfiguration()
    {
    }

    /**
     * 返回生命周期的规则集合
     * @return 生命周期的规则集合
     */
    public List<Rule> getRules()
    {
        return rules;
    }

    /**
     * 添加生命周期的规则
     * @param rule 生命周期的规则
     */
    public void addRule(Rule rule)
    {
        this.rules.add(rule);
    }

    /**
     * 创建一个桶的生命周期的规则
     * @param id
     * @param prefix
     * @param enabled
     * @return rule 生命周期的规则
     */
    public Rule newRule(String id, String prefix, Boolean enabled)
    {
        Rule rule = this.new Rule(id, prefix, enabled);
        this.rules.add(rule);
        return rule;
    }

    /**
     * 生命周期过期时间对象
     */
    public abstract class TimeEvent
    {
        protected Integer days;

        protected Date date;

        public TimeEvent()
        {
        }

        public TimeEvent(Integer days)
        {
            this.days = days;
        }

        public TimeEvent(Date date)
        {
            this.date = date;
        }

        public Integer getDays()
        {
            return days;
        }

        public void setDays(Integer days)
        {
            this.days = days;
            this.date = null;
        }

        public Date getDate()
        {
            return date;
        }

        public void setDate(Date date)
        {
            this.date = date;
            this.days = null;
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (!(o instanceof TimeEvent))
            {
                return false;
            }
            final TimeEvent timeEvent = (TimeEvent) o;
            if (date != null ? !date.equals(timeEvent.date) : timeEvent.date != null)
            {
                return false;
            }
            if (days != null ? !days.equals(timeEvent.days) : timeEvent.days != null)
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            int result = days != null ? days.hashCode() : 0;
            result = 31 * result + (date != null ? date.hashCode() : 0);
            return result;
        }
    }

    /**
     * 生命周期过期时间对象
     */
    public class Expiration extends TimeEvent
    {

        public Expiration()
        {
        }

        public Expiration(Date date)
        {
            super(date);
        }

        public Expiration(Integer days)
        {
            super(days);
        }
    }

    // OceanStor UDS V100R002C01SPC100暂时不支持Transition
    // public class Transition extends TimeEvent {
    // protected String storageClass = STORAGE_CLASS_GLACIER;
    //
    // public Transition() {
    // super();
    // }
    //
    // public Transition(Date date, String storageClass) {
    // super(date);
    // this.storageClass = storageClass;
    // }
    //
    // public Transition(Integer days, String storageClass) {
    // super(days);
    // this.storageClass = storageClass;
    // }
    //
    // public String getStorageClass() {
    // return storageClass;
    // }
    //
    // public void setStorageClass(String storageClass) {
    // this.storageClass = storageClass;
    // }
    // }

    /**
     * 描述生命周期规则
     */
    public class Rule
    {
        protected String id;

        protected String prefix;

        protected Boolean enabled;

        // protected Transition transition; //OceanStor UDS
        // V100R002C01SPC100暂时不支持Transition
        protected Expiration expiration;

        /**
         * 无参构造方法
         */
        public Rule()
        {
        }
        
        /**
         * @param id 规则id
         * @param prefix 对象名前缀
         * @param enabled
         */
        public Rule(String id, String prefix, Boolean enabled)
        {
            this.id = id;
            this.prefix = prefix;
            this.enabled = enabled;
        }
        
        /**
         * 创建（生命周期过期）时间对象
         * @return 过期时间
         */
        public Expiration newExpiration()
        {
            this.expiration = new Expiration();
            return this.expiration;
        }

        // public Transition newTransition() {
        // this.transition = new Transition();
        // return this.transition;
        // }

        /**
         * 返回规则id
         * @return 规则id
         */
        public String getId()
        {
            return id;
        }

        /**
         * 设置规则id
         * @param id 规则id
         */
        public void setId(String id)
        {
            this.id = id;
        }

        /**
         * 返回对象前缀
         * @return 对象名前缀
         */
        public String getPrefix()
        {
            return prefix;
        }

        /**
         * 设置对象前缀
         * @param prefix 对象名前缀
         */
        public void setPrefix(String prefix)
        {
            this.prefix = prefix;
        }

        /**
         * 返回规则是否启用
         * @return true规则已经启用，false规则未启用
         */
        public Boolean getEnabled()
        {
            return enabled;
        }

        /**
         * 设置当前规则是否启用
         * @param enabled 设置为true，则表示启用这条规则，设置为false，则表示停用这条规则
         */
        public void setEnabled(Boolean enabled)
        {
            this.enabled = enabled;
        }

        // public Transition getTransition() {
        // return transition;
        // }

        // public void setTransition(Transition transition) {
        // this.transition = transition;
        // }

        /**
         * 返回生命周期配置的过期时间
         * @return 生命周期配置的过期时间
         */
        public Expiration getExpiration()
        {
            return expiration;
        }

        /**
         * 设置生命周期配置的过期时间
         * @param expiration 生命周期配置中表示过期时间
         */
        public void setExpiration(Expiration expiration)
        {
            this.expiration = expiration;
        }

        /**
         * 比较两条规则是否相同
         */
        @Override
        public boolean equals(final Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            final Rule rule = (Rule) o;
            if (expiration != null ? !expiration.equals(rule.expiration) : rule.expiration != null)
            {
                return false;
            }
            // if(transition != null ? !transition.equals(rule.transition) :
            // rule.transition != null) {
            // return false;
            // }
            return true;
        }

        @Override
        public int hashCode()
        {
            // int result = transition != null ? transition.hashCode() : 0;

            int result = 0;
            result = 31 * result + (expiration != null ? expiration.hashCode() : 0);
            return result;
        }
    }

    /**
     * 比较两个生命周期配置是否相同
     */
    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        final LifecycleConfiguration that = (LifecycleConfiguration) o;
        if (rules != null ? !rules.equals(that.rules) : that.rules != null)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode()
    {
        return rules != null ? rules.hashCode() : 0;
    }
}
