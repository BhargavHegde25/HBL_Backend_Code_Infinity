--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1 (Debian 13.1-1.pgdg100+1)
-- Dumped by pg_dump version 13.0

-- Started on 2021-01-08 12:33:00

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 200 (class 1259 OID 17983)
-- Name: admin_event_entity; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].admin_event_entity (
    id character varying(36) NOT NULL,
    admin_event_time bigint,
    realm_id character varying(255),
    operation_type character varying(255),
    auth_realm_id character varying(255),
    auth_client_id character varying(255),
    auth_user_id character varying(255),
    ip_address character varying(255),
    resource_path character varying(2550),
    representation text,
    error character varying(255),
    resource_type character varying(64)
);


ALTER TABLE [#{dbxschemaname}].admin_event_entity OWNER TO [#{dbxusername}];

--
-- TOC entry 201 (class 1259 OID 17989)
-- Name: associated_policy; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].associated_policy (
    policy_id character varying(36) NOT NULL,
    associated_policy_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].associated_policy OWNER TO [#{dbxusername}];

--
-- TOC entry 202 (class 1259 OID 17992)
-- Name: authentication_execution; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].authentication_execution (
    id character varying(36) NOT NULL,
    alias character varying(255),
    authenticator character varying(36),
    realm_id character varying(36),
    flow_id character varying(36),
    requirement integer,
    priority integer,
    authenticator_flow boolean DEFAULT false NOT NULL,
    auth_flow_id character varying(36),
    auth_config character varying(36)
);


ALTER TABLE [#{dbxschemaname}].authentication_execution OWNER TO [#{dbxusername}];

--
-- TOC entry 203 (class 1259 OID 17996)
-- Name: authentication_flow; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].authentication_flow (
    id character varying(36) NOT NULL,
    alias character varying(255),
    description character varying(255),
    realm_id character varying(36),
    provider_id character varying(36) DEFAULT 'basic-flow'::character varying NOT NULL,
    top_level boolean DEFAULT false NOT NULL,
    built_in boolean DEFAULT false NOT NULL
);


ALTER TABLE [#{dbxschemaname}].authentication_flow OWNER TO [#{dbxusername}];

--
-- TOC entry 204 (class 1259 OID 18005)
-- Name: authenticator_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].authenticator_config (
    id character varying(36) NOT NULL,
    alias character varying(255),
    realm_id character varying(36)
);


ALTER TABLE [#{dbxschemaname}].authenticator_config OWNER TO [#{dbxusername}];

--
-- TOC entry 205 (class 1259 OID 18008)
-- Name: authenticator_config_entry; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].authenticator_config_entry (
    authenticator_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].authenticator_config_entry OWNER TO [#{dbxusername}];

--
-- TOC entry 206 (class 1259 OID 18014)
-- Name: broker_link; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].broker_link (
    identity_provider character varying(255) NOT NULL,
    storage_provider_id character varying(255),
    realm_id character varying(36) NOT NULL,
    broker_user_id character varying(255),
    broker_username character varying(255),
    token text,
    user_id character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].broker_link OWNER TO [#{dbxusername}];

--
-- TOC entry 207 (class 1259 OID 18020)
-- Name: client; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client (
    id character varying(36) NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    full_scope_allowed boolean DEFAULT false NOT NULL,
    client_id character varying(255),
    not_before integer,
    public_client boolean DEFAULT false NOT NULL,
    secret character varying(255),
    base_url character varying(255),
    bearer_only boolean DEFAULT false NOT NULL,
    management_url character varying(255),
    surrogate_auth_required boolean DEFAULT false NOT NULL,
    realm_id character varying(36),
    protocol character varying(255),
    node_rereg_timeout integer DEFAULT 0,
    frontchannel_logout boolean DEFAULT false NOT NULL,
    consent_required boolean DEFAULT false NOT NULL,
    name character varying(255),
    service_accounts_enabled boolean DEFAULT false NOT NULL,
    client_authenticator_type character varying(255),
    root_url character varying(255),
    description character varying(255),
    registration_token character varying(255),
    standard_flow_enabled boolean DEFAULT true NOT NULL,
    implicit_flow_enabled boolean DEFAULT false NOT NULL,
    direct_access_grants_enabled boolean DEFAULT false NOT NULL,
    always_display_in_console boolean DEFAULT false NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client OWNER TO [#{dbxusername}];

--
-- TOC entry 208 (class 1259 OID 18039)
-- Name: client_attributes; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_attributes (
    client_id character varying(36) NOT NULL,
    value character varying(4000),
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_attributes OWNER TO [#{dbxusername}];

--
-- TOC entry 209 (class 1259 OID 18045)
-- Name: client_auth_flow_bindings; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_auth_flow_bindings (
    client_id character varying(36) NOT NULL,
    flow_id character varying(36),
    binding_name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_auth_flow_bindings OWNER TO [#{dbxusername}];

--
-- TOC entry 210 (class 1259 OID 18048)
-- Name: client_default_roles; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_default_roles (
    client_id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_default_roles OWNER TO [#{dbxusername}];

--
-- TOC entry 211 (class 1259 OID 18051)
-- Name: client_initial_access; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_initial_access (
    id character varying(36) NOT NULL,
    realm_id character varying(36) NOT NULL,
    "timestamp" integer,
    expiration integer,
    count integer,
    remaining_count integer
);


ALTER TABLE [#{dbxschemaname}].client_initial_access OWNER TO [#{dbxusername}];

--
-- TOC entry 212 (class 1259 OID 18054)
-- Name: client_node_registrations; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_node_registrations (
    client_id character varying(36) NOT NULL,
    value integer,
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_node_registrations OWNER TO [#{dbxusername}];

--
-- TOC entry 213 (class 1259 OID 18057)
-- Name: client_scope; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_scope (
    id character varying(36) NOT NULL,
    name character varying(255),
    realm_id character varying(36),
    description character varying(255),
    protocol character varying(255)
);


ALTER TABLE [#{dbxschemaname}].client_scope OWNER TO [#{dbxusername}];

--
-- TOC entry 214 (class 1259 OID 18063)
-- Name: client_scope_attributes; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_scope_attributes (
    scope_id character varying(36) NOT NULL,
    value character varying(2048),
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_scope_attributes OWNER TO [#{dbxusername}];

--
-- TOC entry 215 (class 1259 OID 18069)
-- Name: client_scope_client; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_scope_client (
    client_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL,
    default_scope boolean DEFAULT false NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_scope_client OWNER TO [#{dbxusername}];

--
-- TOC entry 216 (class 1259 OID 18073)
-- Name: client_scope_role_mapping; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_scope_role_mapping (
    scope_id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_scope_role_mapping OWNER TO [#{dbxusername}];

--
-- TOC entry 217 (class 1259 OID 18076)
-- Name: client_session; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_session (
    id character varying(36) NOT NULL,
    client_id character varying(36),
    redirect_uri character varying(255),
    state character varying(255),
    "timestamp" integer,
    session_id character varying(36),
    auth_method character varying(255),
    realm_id character varying(255),
    auth_user_id character varying(36),
    current_action character varying(36)
);


ALTER TABLE [#{dbxschemaname}].client_session OWNER TO [#{dbxusername}];

--
-- TOC entry 218 (class 1259 OID 18082)
-- Name: client_session_auth_status; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_session_auth_status (
    authenticator character varying(36) NOT NULL,
    status integer,
    client_session character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_session_auth_status OWNER TO [#{dbxusername}];

--
-- TOC entry 219 (class 1259 OID 18085)
-- Name: client_session_note; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_session_note (
    name character varying(255) NOT NULL,
    value character varying(255),
    client_session character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_session_note OWNER TO [#{dbxusername}];

--
-- TOC entry 220 (class 1259 OID 18091)
-- Name: client_session_prot_mapper; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_session_prot_mapper (
    protocol_mapper_id character varying(36) NOT NULL,
    client_session character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_session_prot_mapper OWNER TO [#{dbxusername}];

--
-- TOC entry 221 (class 1259 OID 18094)
-- Name: client_session_role; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_session_role (
    role_id character varying(255) NOT NULL,
    client_session character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_session_role OWNER TO [#{dbxusername}];

--
-- TOC entry 222 (class 1259 OID 18097)
-- Name: client_user_session_note; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].client_user_session_note (
    name character varying(255) NOT NULL,
    value character varying(2048),
    client_session character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].client_user_session_note OWNER TO [#{dbxusername}];

--
-- TOC entry 223 (class 1259 OID 18103)
-- Name: component; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].component (
    id character varying(36) NOT NULL,
    name character varying(255),
    parent_id character varying(36),
    provider_id character varying(36),
    provider_type character varying(255),
    realm_id character varying(36),
    sub_type character varying(255)
);


ALTER TABLE [#{dbxschemaname}].component OWNER TO [#{dbxusername}];

--
-- TOC entry 224 (class 1259 OID 18109)
-- Name: component_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].component_config (
    id character varying(36) NOT NULL,
    component_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(4000)
);


ALTER TABLE [#{dbxschemaname}].component_config OWNER TO [#{dbxusername}];

--
-- TOC entry 225 (class 1259 OID 18115)
-- Name: composite_role; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].composite_role (
    composite character varying(36) NOT NULL,
    child_role character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].composite_role OWNER TO [#{dbxusername}];

--
-- TOC entry 226 (class 1259 OID 18118)
-- Name: credential; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].credential (
    id character varying(36) NOT NULL,
    salt bytea,
    type character varying(255),
    user_id character varying(36),
    created_date bigint,
    user_label character varying(255),
    secret_data text,
    credential_data text,
    priority integer
);


ALTER TABLE [#{dbxschemaname}].credential OWNER TO [#{dbxusername}];

--
-- TOC entry 227 (class 1259 OID 18124)
-- Name: databasechangelog; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE [#{dbxschemaname}].databasechangelog OWNER TO [#{dbxusername}];

--
-- TOC entry 228 (class 1259 OID 18130)
-- Name: databasechangeloglock; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE [#{dbxschemaname}].databasechangeloglock OWNER TO [#{dbxusername}];

--
-- TOC entry 229 (class 1259 OID 18133)
-- Name: default_client_scope; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].default_client_scope (
    realm_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL,
    default_scope boolean DEFAULT false NOT NULL
);


ALTER TABLE [#{dbxschemaname}].default_client_scope OWNER TO [#{dbxusername}];

--
-- TOC entry 230 (class 1259 OID 18137)
-- Name: event_entity; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].event_entity (
    id character varying(36) NOT NULL,
    client_id character varying(255),
    details_json character varying(2550),
    error character varying(255),
    ip_address character varying(255),
    realm_id character varying(255),
    session_id character varying(255),
    event_time bigint,
    type character varying(255),
    user_id character varying(255)
);


ALTER TABLE [#{dbxschemaname}].event_entity OWNER TO [#{dbxusername}];

--
-- TOC entry 231 (class 1259 OID 18143)
-- Name: fed_user_attribute; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].fed_user_attribute (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36),
    value character varying(2024)
);


ALTER TABLE [#{dbxschemaname}].fed_user_attribute OWNER TO [#{dbxusername}];

--
-- TOC entry 232 (class 1259 OID 18149)
-- Name: fed_user_consent; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].fed_user_consent (
    id character varying(36) NOT NULL,
    client_id character varying(255),
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36),
    created_date bigint,
    last_updated_date bigint,
    client_storage_provider character varying(36),
    external_client_id character varying(255)
);


ALTER TABLE [#{dbxschemaname}].fed_user_consent OWNER TO [#{dbxusername}];

--
-- TOC entry 233 (class 1259 OID 18155)
-- Name: fed_user_consent_cl_scope; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].fed_user_consent_cl_scope (
    user_consent_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].fed_user_consent_cl_scope OWNER TO [#{dbxusername}];

--
-- TOC entry 234 (class 1259 OID 18158)
-- Name: fed_user_credential; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].fed_user_credential (
    id character varying(36) NOT NULL,
    salt bytea,
    type character varying(255),
    created_date bigint,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36),
    user_label character varying(255),
    secret_data text,
    credential_data text,
    priority integer
);


ALTER TABLE [#{dbxschemaname}].fed_user_credential OWNER TO [#{dbxusername}];

--
-- TOC entry 235 (class 1259 OID 18164)
-- Name: fed_user_group_membership; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].fed_user_group_membership (
    group_id character varying(36) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36)
);


ALTER TABLE [#{dbxschemaname}].fed_user_group_membership OWNER TO [#{dbxusername}];

--
-- TOC entry 236 (class 1259 OID 18167)
-- Name: fed_user_required_action; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].fed_user_required_action (
    required_action character varying(255) DEFAULT ' '::character varying NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36)
);


ALTER TABLE [#{dbxschemaname}].fed_user_required_action OWNER TO [#{dbxusername}];

--
-- TOC entry 237 (class 1259 OID 18174)
-- Name: fed_user_role_mapping; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].fed_user_role_mapping (
    role_id character varying(36) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36)
);


ALTER TABLE [#{dbxschemaname}].fed_user_role_mapping OWNER TO [#{dbxusername}];

--
-- TOC entry 238 (class 1259 OID 18177)
-- Name: federated_identity; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].federated_identity (
    identity_provider character varying(255) NOT NULL,
    realm_id character varying(36),
    federated_user_id character varying(255),
    federated_username character varying(255),
    token text,
    user_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].federated_identity OWNER TO [#{dbxusername}];

--
-- TOC entry 239 (class 1259 OID 18183)
-- Name: federated_user; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].federated_user (
    id character varying(255) NOT NULL,
    storage_provider_id character varying(255),
    realm_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].federated_user OWNER TO [#{dbxusername}];

--
-- TOC entry 240 (class 1259 OID 18189)
-- Name: group_attribute; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].group_attribute (
    id character varying(36) DEFAULT 'sybase-needs-something-here'::character varying NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255),
    group_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].group_attribute OWNER TO [#{dbxusername}];

--
-- TOC entry 241 (class 1259 OID 18196)
-- Name: group_role_mapping; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].group_role_mapping (
    role_id character varying(36) NOT NULL,
    group_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].group_role_mapping OWNER TO [#{dbxusername}];

--
-- TOC entry 242 (class 1259 OID 18199)
-- Name: identity_provider; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].identity_provider (
    internal_id character varying(36) NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    provider_alias character varying(255),
    provider_id character varying(255),
    store_token boolean DEFAULT false NOT NULL,
    authenticate_by_default boolean DEFAULT false NOT NULL,
    realm_id character varying(36),
    add_token_role boolean DEFAULT true NOT NULL,
    trust_email boolean DEFAULT false NOT NULL,
    first_broker_login_flow_id character varying(36),
    post_broker_login_flow_id character varying(36),
    provider_display_name character varying(255),
    link_only boolean DEFAULT false NOT NULL
);


ALTER TABLE [#{dbxschemaname}].identity_provider OWNER TO [#{dbxusername}];

--
-- TOC entry 243 (class 1259 OID 18211)
-- Name: identity_provider_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].identity_provider_config (
    identity_provider_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].identity_provider_config OWNER TO [#{dbxusername}];

--
-- TOC entry 244 (class 1259 OID 18217)
-- Name: identity_provider_mapper; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].identity_provider_mapper (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    idp_alias character varying(255) NOT NULL,
    idp_mapper_name character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].identity_provider_mapper OWNER TO [#{dbxusername}];

--
-- TOC entry 245 (class 1259 OID 18223)
-- Name: idp_mapper_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].idp_mapper_config (
    idp_mapper_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].idp_mapper_config OWNER TO [#{dbxusername}];

--
-- TOC entry 246 (class 1259 OID 18229)
-- Name: [#{dbxusername}]_group; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].[#{dbxusername}]_group (
    id character varying(36) NOT NULL,
    name character varying(255),
    parent_group character varying(36) NOT NULL,
    realm_id character varying(36)
);


ALTER TABLE [#{dbxschemaname}].[#{dbxusername}]_group OWNER TO [#{dbxusername}];

--
-- TOC entry 247 (class 1259 OID 18232)
-- Name: [#{dbxusername}]_role; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].[#{dbxusername}]_role (
    id character varying(36) NOT NULL,
    client_realm_constraint character varying(255),
    client_role boolean DEFAULT false NOT NULL,
    description character varying(255),
    name character varying(255),
    realm_id character varying(255),
    client character varying(36),
    realm character varying(36)
);


ALTER TABLE [#{dbxschemaname}].[#{dbxusername}]_role OWNER TO [#{dbxusername}];

--
-- TOC entry 248 (class 1259 OID 18239)
-- Name: migration_model; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].migration_model (
    id character varying(36) NOT NULL,
    version character varying(36),
    update_time bigint DEFAULT 0 NOT NULL
);


ALTER TABLE [#{dbxschemaname}].migration_model OWNER TO [#{dbxusername}];

--
-- TOC entry 249 (class 1259 OID 18243)
-- Name: offline_client_session; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].offline_client_session (
    user_session_id character varying(36) NOT NULL,
    client_id character varying(255) NOT NULL,
    offline_flag character varying(4) NOT NULL,
    "timestamp" integer,
    data text,
    client_storage_provider character varying(36) DEFAULT 'local'::character varying NOT NULL,
    external_client_id character varying(255) DEFAULT 'local'::character varying NOT NULL
);


ALTER TABLE [#{dbxschemaname}].offline_client_session OWNER TO [#{dbxusername}];

--
-- TOC entry 250 (class 1259 OID 18251)
-- Name: offline_user_session; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].offline_user_session (
    user_session_id character varying(36) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    created_on integer NOT NULL,
    offline_flag character varying(4) NOT NULL,
    data text,
    last_session_refresh integer DEFAULT 0 NOT NULL
);


ALTER TABLE [#{dbxschemaname}].offline_user_session OWNER TO [#{dbxusername}];

--
-- TOC entry 251 (class 1259 OID 18258)
-- Name: policy_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].policy_config (
    policy_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value text
);


ALTER TABLE [#{dbxschemaname}].policy_config OWNER TO [#{dbxusername}];

--
-- TOC entry 252 (class 1259 OID 18264)
-- Name: protocol_mapper; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].protocol_mapper (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    protocol character varying(255) NOT NULL,
    protocol_mapper_name character varying(255) NOT NULL,
    client_id character varying(36),
    client_scope_id character varying(36)
);


ALTER TABLE [#{dbxschemaname}].protocol_mapper OWNER TO [#{dbxusername}];

--
-- TOC entry 253 (class 1259 OID 18270)
-- Name: protocol_mapper_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].protocol_mapper_config (
    protocol_mapper_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].protocol_mapper_config OWNER TO [#{dbxusername}];

--
-- TOC entry 254 (class 1259 OID 18276)
-- Name: realm; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].realm (
    id character varying(36) NOT NULL,
    access_code_lifespan integer,
    user_action_lifespan integer,
    access_token_lifespan integer,
    account_theme character varying(255),
    admin_theme character varying(255),
    email_theme character varying(255),
    enabled boolean DEFAULT false NOT NULL,
    events_enabled boolean DEFAULT false NOT NULL,
    events_expiration bigint,
    login_theme character varying(255),
    name character varying(255),
    not_before integer,
    password_policy character varying(2550),
    registration_allowed boolean DEFAULT false NOT NULL,
    remember_me boolean DEFAULT false NOT NULL,
    reset_password_allowed boolean DEFAULT false NOT NULL,
    social boolean DEFAULT false NOT NULL,
    ssl_required character varying(255),
    sso_idle_timeout integer,
    sso_max_lifespan integer,
    update_profile_on_soc_login boolean DEFAULT false NOT NULL,
    verify_email boolean DEFAULT false NOT NULL,
    master_admin_client character varying(36),
    login_lifespan integer,
    internationalization_enabled boolean DEFAULT false NOT NULL,
    default_locale character varying(255),
    reg_email_as_username boolean DEFAULT false NOT NULL,
    admin_events_enabled boolean DEFAULT false NOT NULL,
    admin_events_details_enabled boolean DEFAULT false NOT NULL,
    edit_username_allowed boolean DEFAULT false NOT NULL,
    otp_policy_counter integer DEFAULT 0,
    otp_policy_window integer DEFAULT 1,
    otp_policy_period integer DEFAULT 30,
    otp_policy_digits integer DEFAULT 6,
    otp_policy_alg character varying(36) DEFAULT 'HmacSHA1'::character varying,
    otp_policy_type character varying(36) DEFAULT 'totp'::character varying,
    browser_flow character varying(36),
    registration_flow character varying(36),
    direct_grant_flow character varying(36),
    reset_credentials_flow character varying(36),
    client_auth_flow character varying(36),
    offline_session_idle_timeout integer DEFAULT 0,
    revoke_refresh_token boolean DEFAULT false NOT NULL,
    access_token_life_implicit integer DEFAULT 0,
    login_with_email_allowed boolean DEFAULT true NOT NULL,
    duplicate_emails_allowed boolean DEFAULT false NOT NULL,
    docker_auth_flow character varying(36),
    refresh_token_max_reuse integer DEFAULT 0,
    allow_user_managed_access boolean DEFAULT false NOT NULL,
    sso_max_lifespan_remember_me integer DEFAULT 0 NOT NULL,
    sso_idle_timeout_remember_me integer DEFAULT 0 NOT NULL
);


ALTER TABLE [#{dbxschemaname}].realm OWNER TO [#{dbxusername}];

--
-- TOC entry 255 (class 1259 OID 18310)
-- Name: realm_attribute; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].realm_attribute (
    name character varying(255) NOT NULL,
    value character varying(255),
    realm_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].realm_attribute OWNER TO [#{dbxusername}];

--
-- TOC entry 256 (class 1259 OID 18316)
-- Name: realm_default_groups; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].realm_default_groups (
    realm_id character varying(36) NOT NULL,
    group_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].realm_default_groups OWNER TO [#{dbxusername}];

--
-- TOC entry 257 (class 1259 OID 18319)
-- Name: realm_default_roles; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].realm_default_roles (
    realm_id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].realm_default_roles OWNER TO [#{dbxusername}];

--
-- TOC entry 258 (class 1259 OID 18322)
-- Name: realm_enabled_event_types; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].realm_enabled_event_types (
    realm_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].realm_enabled_event_types OWNER TO [#{dbxusername}];

--
-- TOC entry 259 (class 1259 OID 18325)
-- Name: realm_events_listeners; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].realm_events_listeners (
    realm_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].realm_events_listeners OWNER TO [#{dbxusername}];

--
-- TOC entry 260 (class 1259 OID 18328)
-- Name: realm_required_credential; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].realm_required_credential (
    type character varying(255) NOT NULL,
    form_label character varying(255),
    input boolean DEFAULT false NOT NULL,
    secret boolean DEFAULT false NOT NULL,
    realm_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].realm_required_credential OWNER TO [#{dbxusername}];

--
-- TOC entry 261 (class 1259 OID 18336)
-- Name: realm_smtp_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].realm_smtp_config (
    realm_id character varying(36) NOT NULL,
    value character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].realm_smtp_config OWNER TO [#{dbxusername}];

--
-- TOC entry 262 (class 1259 OID 18342)
-- Name: realm_supported_locales; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].realm_supported_locales (
    realm_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].realm_supported_locales OWNER TO [#{dbxusername}];

--
-- TOC entry 263 (class 1259 OID 18345)
-- Name: redirect_uris; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].redirect_uris (
    client_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].redirect_uris OWNER TO [#{dbxusername}];

--
-- TOC entry 264 (class 1259 OID 18348)
-- Name: required_action_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].required_action_config (
    required_action_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].required_action_config OWNER TO [#{dbxusername}];

--
-- TOC entry 265 (class 1259 OID 18354)
-- Name: required_action_provider; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].required_action_provider (
    id character varying(36) NOT NULL,
    alias character varying(255),
    name character varying(255),
    realm_id character varying(36),
    enabled boolean DEFAULT false NOT NULL,
    default_action boolean DEFAULT false NOT NULL,
    provider_id character varying(255),
    priority integer
);


ALTER TABLE [#{dbxschemaname}].required_action_provider OWNER TO [#{dbxusername}];

--
-- TOC entry 266 (class 1259 OID 18362)
-- Name: resource_attribute; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].resource_attribute (
    id character varying(36) DEFAULT 'sybase-needs-something-here'::character varying NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255),
    resource_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].resource_attribute OWNER TO [#{dbxusername}];

--
-- TOC entry 267 (class 1259 OID 18369)
-- Name: resource_policy; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].resource_policy (
    resource_id character varying(36) NOT NULL,
    policy_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].resource_policy OWNER TO [#{dbxusername}];

--
-- TOC entry 268 (class 1259 OID 18372)
-- Name: resource_scope; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].resource_scope (
    resource_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].resource_scope OWNER TO [#{dbxusername}];

--
-- TOC entry 269 (class 1259 OID 18375)
-- Name: resource_server; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].resource_server (
    id character varying(36) NOT NULL,
    allow_rs_remote_mgmt boolean DEFAULT false NOT NULL,
    policy_enforce_mode character varying(15) NOT NULL,
    decision_strategy smallint DEFAULT 1 NOT NULL
);


ALTER TABLE [#{dbxschemaname}].resource_server OWNER TO [#{dbxusername}];

--
-- TOC entry 270 (class 1259 OID 18380)
-- Name: resource_server_perm_ticket; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].resource_server_perm_ticket (
    id character varying(36) NOT NULL,
    owner character varying(255) NOT NULL,
    requester character varying(255) NOT NULL,
    created_timestamp bigint NOT NULL,
    granted_timestamp bigint,
    resource_id character varying(36) NOT NULL,
    scope_id character varying(36),
    resource_server_id character varying(36) NOT NULL,
    policy_id character varying(36)
);


ALTER TABLE [#{dbxschemaname}].resource_server_perm_ticket OWNER TO [#{dbxusername}];

--
-- TOC entry 271 (class 1259 OID 18386)
-- Name: resource_server_policy; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].resource_server_policy (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    type character varying(255) NOT NULL,
    decision_strategy character varying(20),
    logic character varying(20),
    resource_server_id character varying(36) NOT NULL,
    owner character varying(255)
);


ALTER TABLE [#{dbxschemaname}].resource_server_policy OWNER TO [#{dbxusername}];

--
-- TOC entry 272 (class 1259 OID 18392)
-- Name: resource_server_resource; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].resource_server_resource (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    type character varying(255),
    icon_uri character varying(255),
    owner character varying(255) NOT NULL,
    resource_server_id character varying(36) NOT NULL,
    owner_managed_access boolean DEFAULT false NOT NULL,
    display_name character varying(255)
);


ALTER TABLE [#{dbxschemaname}].resource_server_resource OWNER TO [#{dbxusername}];

--
-- TOC entry 273 (class 1259 OID 18399)
-- Name: resource_server_scope; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].resource_server_scope (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    icon_uri character varying(255),
    resource_server_id character varying(36) NOT NULL,
    display_name character varying(255)
);


ALTER TABLE [#{dbxschemaname}].resource_server_scope OWNER TO [#{dbxusername}];

--
-- TOC entry 274 (class 1259 OID 18405)
-- Name: resource_uris; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].resource_uris (
    resource_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].resource_uris OWNER TO [#{dbxusername}];

--
-- TOC entry 275 (class 1259 OID 18408)
-- Name: role_attribute; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].role_attribute (
    id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255)
);


ALTER TABLE [#{dbxschemaname}].role_attribute OWNER TO [#{dbxusername}];

--
-- TOC entry 276 (class 1259 OID 18414)
-- Name: scope_mapping; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].scope_mapping (
    client_id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].scope_mapping OWNER TO [#{dbxusername}];

--
-- TOC entry 277 (class 1259 OID 18417)
-- Name: scope_policy; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].scope_policy (
    scope_id character varying(36) NOT NULL,
    policy_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].scope_policy OWNER TO [#{dbxusername}];

--
-- TOC entry 278 (class 1259 OID 18420)
-- Name: user_attribute; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_attribute (
    name character varying(255) NOT NULL,
    value character varying(255),
    user_id character varying(36) NOT NULL,
    id character varying(36) DEFAULT 'sybase-needs-something-here'::character varying NOT NULL
);


ALTER TABLE [#{dbxschemaname}].user_attribute OWNER TO [#{dbxusername}];

--
-- TOC entry 279 (class 1259 OID 18427)
-- Name: user_consent; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_consent (
    id character varying(36) NOT NULL,
    client_id character varying(255),
    user_id character varying(36) NOT NULL,
    created_date bigint,
    last_updated_date bigint,
    client_storage_provider character varying(36),
    external_client_id character varying(255)
);


ALTER TABLE [#{dbxschemaname}].user_consent OWNER TO [#{dbxusername}];

--
-- TOC entry 280 (class 1259 OID 18433)
-- Name: user_consent_client_scope; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_consent_client_scope (
    user_consent_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].user_consent_client_scope OWNER TO [#{dbxusername}];

--
-- TOC entry 281 (class 1259 OID 18436)
-- Name: user_entity; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_entity (
    id character varying(36) NOT NULL,
    email character varying(255),
    email_constraint character varying(255),
    email_verified boolean DEFAULT false NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    federation_link character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    realm_id character varying(255),
    username character varying(255),
    created_timestamp bigint,
    service_account_client_link character varying(255),
    not_before integer DEFAULT 0 NOT NULL
);


ALTER TABLE [#{dbxschemaname}].user_entity OWNER TO [#{dbxusername}];

--
-- TOC entry 282 (class 1259 OID 18445)
-- Name: user_federation_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_federation_config (
    user_federation_provider_id character varying(36) NOT NULL,
    value character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].user_federation_config OWNER TO [#{dbxusername}];

--
-- TOC entry 283 (class 1259 OID 18451)
-- Name: user_federation_mapper; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_federation_mapper (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    federation_provider_id character varying(36) NOT NULL,
    federation_mapper_type character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].user_federation_mapper OWNER TO [#{dbxusername}];

--
-- TOC entry 284 (class 1259 OID 18457)
-- Name: user_federation_mapper_config; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_federation_mapper_config (
    user_federation_mapper_id character varying(36) NOT NULL,
    value character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].user_federation_mapper_config OWNER TO [#{dbxusername}];

--
-- TOC entry 285 (class 1259 OID 18463)
-- Name: user_federation_provider; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_federation_provider (
    id character varying(36) NOT NULL,
    changed_sync_period integer,
    display_name character varying(255),
    full_sync_period integer,
    last_sync integer,
    priority integer,
    provider_name character varying(255),
    realm_id character varying(36)
);


ALTER TABLE [#{dbxschemaname}].user_federation_provider OWNER TO [#{dbxusername}];

--
-- TOC entry 286 (class 1259 OID 18469)
-- Name: user_group_membership; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_group_membership (
    group_id character varying(36) NOT NULL,
    user_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].user_group_membership OWNER TO [#{dbxusername}];

--
-- TOC entry 287 (class 1259 OID 18472)
-- Name: user_required_action; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_required_action (
    user_id character varying(36) NOT NULL,
    required_action character varying(255) DEFAULT ' '::character varying NOT NULL
);


ALTER TABLE [#{dbxschemaname}].user_required_action OWNER TO [#{dbxusername}];

--
-- TOC entry 288 (class 1259 OID 18476)
-- Name: user_role_mapping; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_role_mapping (
    role_id character varying(255) NOT NULL,
    user_id character varying(36) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].user_role_mapping OWNER TO [#{dbxusername}];

--
-- TOC entry 289 (class 1259 OID 18479)
-- Name: user_session; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_session (
    id character varying(36) NOT NULL,
    auth_method character varying(255),
    ip_address character varying(255),
    last_session_refresh integer,
    login_username character varying(255),
    realm_id character varying(255),
    remember_me boolean DEFAULT false NOT NULL,
    started integer,
    user_id character varying(255),
    user_session_state integer,
    broker_session_id character varying(255),
    broker_user_id character varying(255)
);


ALTER TABLE [#{dbxschemaname}].user_session OWNER TO [#{dbxusername}];

--
-- TOC entry 290 (class 1259 OID 18486)
-- Name: user_session_note; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].user_session_note (
    user_session character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(2048)
);


ALTER TABLE [#{dbxschemaname}].user_session_note OWNER TO [#{dbxusername}];

--
-- TOC entry 291 (class 1259 OID 18492)
-- Name: username_login_failure; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].username_login_failure (
    realm_id character varying(36) NOT NULL,
    username character varying(255) NOT NULL,
    failed_login_not_before integer,
    last_failure bigint,
    last_ip_failure character varying(255),
    num_failures integer
);


ALTER TABLE [#{dbxschemaname}].username_login_failure OWNER TO [#{dbxusername}];

--
-- TOC entry 292 (class 1259 OID 18498)
-- Name: web_origins; Type: TABLE; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--

CREATE TABLE [#{dbxschemaname}].web_origins (
    client_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE [#{dbxschemaname}].web_origins OWNER TO [#{dbxusername}];

--
-- TOC entry 3811 (class 0 OID 17983)
-- Dependencies: 200
-- Data for Name: admin_event_entity; Type: TABLE DATA; Schema: [#{dbxschemaname}]; Owner: [#{dbxusername}]
--
