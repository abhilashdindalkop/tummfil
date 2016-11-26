# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                        bigint auto_increment not null,
  type                      varchar(50) not null,
  is_image_exists           tinyint(1) default 0,
  created_time              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  constraint pk_category primary key (id))
;

create table cities (
  id                        bigint auto_increment not null,
  city                      varchar(255),
  state                     varchar(255),
  country                   varchar(255),
  created_time              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  constraint pk_cities primary key (id))
;

create table delivery_boy_session (
  id                        bigint auto_increment not null,
  delivery_boy_id           bigint not null,
  encrypted_id              varchar(255) not null,
  token                     varchar(255) not null,
  device_id                 varchar(255) not null,
  device_token              varchar(255),
  end_point_arn             varchar(1023),
  app_version               varchar(45),
  badge_count               integer,
  device_type_id            integer not null,
  login_datetime            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  constraint uq_delivery_boy_session_token unique (token),
  constraint uq_delivery_boy_session_device_id unique (device_id),
  constraint uq_delivery_boy_session_device_token unique (device_token),
  constraint pk_delivery_boy_session primary key (id))
;

create table delivery_boys (
  id                        bigint auto_increment not null,
  encrypted_id              varchar(255) not null,
  name                      varchar(255),
  vendor_id                 bigint,
  address                   TEXT,
  locality_id_id            bigint,
  city_id                   bigint not null,
  phone_no                  varchar(255),
  password                  varchar(255),
  is_deleted                tinyint(1) default 0,
  logged_in_hours           double,
  constraint uq_delivery_boys_encrypted_id unique (encrypted_id),
  constraint uq_delivery_boys_phone_no unique (phone_no),
  constraint pk_delivery_boys primary key (id))
;

create table device_type (
  id                        integer auto_increment not null,
  device_name               varchar(50) not null,
  constraint pk_device_type primary key (id))
;

create table locality (
  id                        bigint auto_increment not null,
  address                   varchar(255),
  latitude                  double,
  longitude                 double,
  pincode                   integer,
  city_id                   bigint not null,
  created_time              TIMESTAMP,
  constraint pk_locality primary key (id))
;

create table ordered_products (
  id                        bigint auto_increment not null,
  order_id                  bigint not null,
  product_id                bigint not null,
  quantity                  integer,
  price                     double,
  created_time              TIMESTAMP,
  constraint pk_ordered_products primary key (id))
;

create table orders (
  id                        bigint auto_increment not null,
  order_id                  varchar(255) not null,
  vendor_id                 bigint not null,
  user_id                   bigint not null,
  city_id                   bigint not null,
  name                      varchar(255),
  extra_fee                 double,
  total_price               double,
  company_fee               double,
  status                    integer,
  order_type                integer,
  payment_type              integer,
  payment_status            integer,
  address                   TEXT,
  pincode                   integer,
  phone_no                  varchar(255),
  latitude                  double,
  longitude                 double,
  description               TEXT,
  delivery_time             TIMESTAMP,
  created_time              TIMESTAMP,
  updated_time              TIMESTAMP,
  constraint uq_orders_order_id unique (order_id),
  constraint pk_orders primary key (id))
;

create table products (
  id                        bigint auto_increment not null,
  product_id                varchar(255) not null,
  vendor_id                 bigint not null,
  name                      varchar(255),
  description               TEXT,
  image_url                 varchar(255),
  category_id               bigint not null,
  status                    integer,
  product_type              integer,
  unit_type                 integer,
  units                     double,
  price                     double,
  is_deleted                tinyint(1) default 0,
  is_featured               tinyint(1) default 0,
  updated_time              TIMESTAMP,
  created_time              TIMESTAMP,
  constraint uq_products_product_id unique (product_id),
  constraint pk_products primary key (id))
;

create table promotions (
  id                        bigint auto_increment not null,
  description               TEXT,
  vendor_id                 bigint,
  image_url                 varchar(255),
  created_time              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  constraint pk_promotions primary key (id))
;

create table referral (
  id                        bigint auto_increment not null,
  credit                    float not null,
  status                    integer not null,
  created_time              TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
  constraint pk_referral primary key (id))
;

create table tags (
  id                        bigint auto_increment not null,
  name                      varchar(255) not null,
  created_time              TIMESTAMP,
  constraint uq_tags_name unique (name),
  constraint pk_tags primary key (id))
;

create table transactions (
  id                        bigint auto_increment not null,
  encrypted_id              varchar(255) not null,
  order_id                  bigint not null,
  transaction_id            varchar(255),
  amount                    double,
  status                    integer,
  currency                  integer,
  payment_type              integer,
  gateway_status_code       integer,
  gateway_error             varchar(255),
  created_time              TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
  constraint pk_transactions primary key (id))
;

create table user_address (
  id                        bigint auto_increment not null,
  user_id                   bigint not null,
  address                   varchar(255),
  latitude                  double,
  longitude                 double,
  landmark                  varchar(255),
  city_id                   bigint not null,
  address_type              integer,
  constraint pk_user_address primary key (id))
;

create table user_push_notifications (
  id                        bigint auto_increment not null,
  sender_profile_id         varchar(255),
  receiver_profile_id       varchar(255) not null,
  message                   TEXT,
  device_token              varchar(255),
  timestamp                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
  notification_type         integer,
  account_type              integer,
  is_sent                   tinyint(1) default 0,
  constraint pk_user_push_notifications primary key (id))
;

create table user_session (
  id                        bigint auto_increment not null,
  user_id                   bigint not null,
  encrypted_user_id         varchar(255) not null,
  token                     varchar(255) not null,
  device_id                 varchar(255) not null,
  device_token              varchar(255),
  end_point_arn             varchar(1023),
  app_version               varchar(45),
  badge_count               integer,
  device_type_id            integer not null,
  login_datetime            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  constraint uq_user_session_token unique (token),
  constraint uq_user_session_device_id unique (device_id),
  constraint uq_user_session_device_token unique (device_token),
  constraint pk_user_session primary key (id))
;

create table users (
  id                        bigint auto_increment not null,
  encrypted_user_id         varchar(255) not null,
  name                      varchar(255),
  facebook_id               varchar(255),
  address                   TEXT,
  pincode                   varchar(255),
  city_id                   bigint,
  phone_no                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  referral_code             varchar(255),
  wallet_amount             double,
  is_user_verified          tinyint(1) default 0,
  is_deleted                tinyint(1) default 0,
  created_time              TIMESTAMP,
  last_login                TIMESTAMP,
  constraint uq_users_encrypted_user_id unique (encrypted_user_id),
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id))
;

create table vendor_session (
  id                        bigint auto_increment not null,
  vendor_id                 bigint not null,
  encrypted_vendor_id       varchar(255) not null,
  token                     varchar(255) not null,
  device_id                 varchar(255) not null,
  device_token              varchar(255),
  end_point_arn             varchar(1023),
  app_version               varchar(45),
  badge_count               integer,
  device_type_id            integer not null,
  login_datetime            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  constraint uq_vendor_session_token unique (token),
  constraint uq_vendor_session_device_id unique (device_id),
  constraint uq_vendor_session_device_token unique (device_token),
  constraint pk_vendor_session primary key (id))
;

create table vendors (
  id                        bigint auto_increment not null,
  encrypted_vendor_id       varchar(255) not null,
  name                      varchar(255),
  vendor_name               varchar(255),
  vendor_type               integer,
  vendor_address            TEXT,
  city_id                   bigint not null,
  latitude                  double,
  longitude                 double,
  description               TEXT,
  phone_no                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  image_url                 varchar(255),
  is_vendor_available       tinyint(1) default 0,
  is_vendor_verified        tinyint(1) default 0,
  is_deleted                tinyint(1) default 0,
  created_time              TIMESTAMP,
  updated_time              TIMESTAMP,
  last_login                TIMESTAMP,
  constraint uq_vendors_encrypted_vendor_id unique (encrypted_vendor_id),
  constraint uq_vendors_phone_no unique (phone_no),
  constraint uq_vendors_email unique (email),
  constraint pk_vendors primary key (id))
;

alter table delivery_boy_session add constraint fk_delivery_boy_session_deliveryBoy_1 foreign key (delivery_boy_id) references delivery_boys (id) on delete restrict on update restrict;
create index ix_delivery_boy_session_deliveryBoy_1 on delivery_boy_session (delivery_boy_id);
alter table delivery_boy_session add constraint fk_delivery_boy_session_deviceType_2 foreign key (device_type_id) references device_type (id) on delete restrict on update restrict;
create index ix_delivery_boy_session_deviceType_2 on delivery_boy_session (device_type_id);
alter table delivery_boys add constraint fk_delivery_boys_vendor_3 foreign key (vendor_id) references vendors (id) on delete restrict on update restrict;
create index ix_delivery_boys_vendor_3 on delivery_boys (vendor_id);
alter table delivery_boys add constraint fk_delivery_boys_localityId_4 foreign key (locality_id_id) references locality (id) on delete restrict on update restrict;
create index ix_delivery_boys_localityId_4 on delivery_boys (locality_id_id);
alter table delivery_boys add constraint fk_delivery_boys_city_5 foreign key (city_id) references cities (id) on delete restrict on update restrict;
create index ix_delivery_boys_city_5 on delivery_boys (city_id);
alter table locality add constraint fk_locality_city_6 foreign key (city_id) references cities (id) on delete restrict on update restrict;
create index ix_locality_city_6 on locality (city_id);
alter table ordered_products add constraint fk_ordered_products_order_7 foreign key (order_id) references orders (id) on delete restrict on update restrict;
create index ix_ordered_products_order_7 on ordered_products (order_id);
alter table ordered_products add constraint fk_ordered_products_product_8 foreign key (product_id) references products (id) on delete restrict on update restrict;
create index ix_ordered_products_product_8 on ordered_products (product_id);
alter table orders add constraint fk_orders_vendor_9 foreign key (vendor_id) references vendors (id) on delete restrict on update restrict;
create index ix_orders_vendor_9 on orders (vendor_id);
alter table orders add constraint fk_orders_user_10 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_orders_user_10 on orders (user_id);
alter table orders add constraint fk_orders_city_11 foreign key (city_id) references cities (id) on delete restrict on update restrict;
create index ix_orders_city_11 on orders (city_id);
alter table products add constraint fk_products_vendor_12 foreign key (vendor_id) references vendors (id) on delete restrict on update restrict;
create index ix_products_vendor_12 on products (vendor_id);
alter table products add constraint fk_products_category_13 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_products_category_13 on products (category_id);
alter table promotions add constraint fk_promotions_vendor_14 foreign key (vendor_id) references vendors (id) on delete restrict on update restrict;
create index ix_promotions_vendor_14 on promotions (vendor_id);
alter table transactions add constraint fk_transactions_order_15 foreign key (order_id) references orders (id) on delete restrict on update restrict;
create index ix_transactions_order_15 on transactions (order_id);
alter table user_address add constraint fk_user_address_user_16 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_user_address_user_16 on user_address (user_id);
alter table user_address add constraint fk_user_address_city_17 foreign key (city_id) references cities (id) on delete restrict on update restrict;
create index ix_user_address_city_17 on user_address (city_id);
alter table user_session add constraint fk_user_session_user_18 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_user_session_user_18 on user_session (user_id);
alter table user_session add constraint fk_user_session_deviceType_19 foreign key (device_type_id) references device_type (id) on delete restrict on update restrict;
create index ix_user_session_deviceType_19 on user_session (device_type_id);
alter table users add constraint fk_users_city_20 foreign key (city_id) references cities (id) on delete restrict on update restrict;
create index ix_users_city_20 on users (city_id);
alter table vendor_session add constraint fk_vendor_session_vendor_21 foreign key (vendor_id) references vendors (id) on delete restrict on update restrict;
create index ix_vendor_session_vendor_21 on vendor_session (vendor_id);
alter table vendor_session add constraint fk_vendor_session_deviceType_22 foreign key (device_type_id) references device_type (id) on delete restrict on update restrict;
create index ix_vendor_session_deviceType_22 on vendor_session (device_type_id);
alter table vendors add constraint fk_vendors_city_23 foreign key (city_id) references cities (id) on delete restrict on update restrict;
create index ix_vendors_city_23 on vendors (city_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table category;

drop table cities;

drop table delivery_boy_session;

drop table delivery_boys;

drop table device_type;

drop table locality;

drop table ordered_products;

drop table orders;

drop table products;

drop table promotions;

drop table referral;

drop table tags;

drop table transactions;

drop table user_address;

drop table user_push_notifications;

drop table user_session;

drop table users;

drop table vendor_session;

drop table vendors;

SET FOREIGN_KEY_CHECKS=1;

