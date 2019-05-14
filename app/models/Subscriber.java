package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

    @Entity
    public class Subscriber extends Model {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        public Long id;
        public String msisdn;
        @Temporal(TemporalType.TIMESTAMP)
        public Date insertDate;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }

        public Date getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(Date insertDate) {
            this.insertDate = insertDate;
        }
    }
