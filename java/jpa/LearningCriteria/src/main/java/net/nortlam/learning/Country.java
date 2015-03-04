/*
 * 
 * Copyright 2014 Mauricio "Maltron" Leal <maltron@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package net.nortlam.learning;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mauricio "Maltron" Leal */
@Entity(name="Country")
@Table(name="COUNTRY")
@XmlRootElement(name="Country")
@XmlAccessorType(XmlAccessType.FIELD)
public class Country implements Serializable {

    private static final Logger LOG = Logger.getLogger(Country.class.getName());
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COUNTRY_ID", unique = true, nullable = false)
    @XmlAttribute(name="ID", required = false)
    private long ID;
    
    public static final int LENGTH_NAME = 53;
    @Column(name="NAME", length = LENGTH_NAME, unique=true, nullable = false)
    @XmlElement(name="Name", type=String.class, required=true)
    private String name;

    public static final int LENGTH_CODE2 = 2;
    @Column(name="CODE2", columnDefinition = "CHAR(2)", length = LENGTH_CODE2, unique = true, nullable = false)
    private String code2;
    
    public static final int LENGTH_CODE3 = 3;
    @Column(name="CODE3", columnDefinition = "CHAR(3)", length = LENGTH_CODE3, unique = true, nullable = false)
    private String code3;
    
    public Country() {
    }
    
    public Country(String name) {
        this.name = name;
    }

    public Country(String name, String code2, String code3) {
        this.name = name;
        this.code2 = code2;
        this.code3 = code3;
    }

    public Country(long ID, String name, String code2, String code3) {
        this.ID = ID;
        this.name = name;
        this.code2 = code2;
        this.code3 = code3;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getCode3() {
        return code3;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.code2);
        hash = 53 * hash + Objects.hashCode(this.code3);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Country other = (Country) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.code2, other.code2)) {
            return false;
        }
        if (!Objects.equals(this.code3, other.code3)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<Country ID=\"").append(ID).append("\">");
        if(name != null)
            buffer.append("<Name>").append(name).append("</Name>");
        if(code2 != null) 
            buffer.append("<Code2>").append(code2).append("</Code2>");
        if(code3 != null)
            buffer.append("<Code3>").append(code3).append("</Code3>");
        buffer.append("</Country>");
        
        return buffer.toString();
    }
}
