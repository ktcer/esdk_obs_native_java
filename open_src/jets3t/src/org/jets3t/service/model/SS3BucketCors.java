package org.jets3t.service.model;

import java.util.ArrayList;
import java.util.List;

public class SS3BucketCors
{

    private List<SS3CORSRule> rules;

    /**
     * Returns the list of rules that comprise this configuration.
     */
    public List<SS3CORSRule> getRules()
    {
        if(null == rules)
        {
            return rules = new ArrayList<SS3CORSRule>();
        }
        return rules;
    }

    /**
     * Sets the rules that comprise this configuration.
     */
    public void setRules(List<SS3CORSRule> rules)
    {
        this.rules = rules;
    }

    /**
     * Constructs a new {@link BucketCrossOriginConfiguration} object with the
     * rules given.
     * 
     * @param rules
     */
    public SS3BucketCors(List<SS3CORSRule> rules)
    {
        this.rules = rules;
    }

    public SS3BucketCors()
    {
        super();
    }

    /**
     * toXML
     */
    public String toXML()
    {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<CORSConfiguration>";
        for (int i = 0; i < rules.size(); i++)
        {
            xml += "<CORSRule>";
            xml += rules.get(i).toXML();
            xml += "</CORSRule>";
        }
        xml += "</CORSConfiguration>";
        return xml;
    }

    @Override
    public String toString()
    {
        return "SS3BucketCors [rules=" + rules + "]";
    }

}
