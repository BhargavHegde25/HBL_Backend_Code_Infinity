package com.temenos.dbx.party.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.infinity.dbx.dbp.jwt.auth.Authentication;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;

import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.AlternateIdentity;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerEmploymentDetailsDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.Employment;
import com.temenos.dbx.product.dto.PartyAddress;
import com.temenos.dbx.product.dto.PartyDTO;
import com.temenos.dbx.product.dto.PartyIdentifier;
import com.temenos.dbx.product.dto.TaxDetails;
import com.temenos.dbx.product.integration.IntegrationMappings;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.usermanagement.constants.PartyProfileDetailsConstants;
import com.temenos.dbx.usermanagement.dto.PartyDetails;

public class PartyUtils {
private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static LoggerUtil logger;

    private static void initLOG() {
        if (logger == null) {
            logger = new LoggerUtil(PartyUtils.class);
        }
    }

    public static PartyDTO buildAdditionalData(PartyDTO partyDTO, Map<String, Object> map) {
        if (map.get("entityName") != null && StringUtils.isNotBlank((String) map.get("entityName"))) {
            partyDTO.setEntityName(map.get("entityName").toString());
            if(map.get("organisationLegalType")!=null)
            	partyDTO.setOrganisationLegalType(map.get("organisationLegalType").toString());
            partyDTO.setIncorporationCountry(map.get("incorporationCountry").toString());
            partyDTO.setNumberOfEmployees(map.get("numberOfEmployees").toString());
            partyDTO.setDateOfIncorporation(map.get("dateOfIncorporation").toString());

			if (map.get("registrationDetails") != null
					&& StringUtils.isNotBlank((String) map.get("registrationDetails"))) {
				JSONObject registrationDetails = new JSONObject((String) map.get("registrationDetails"));
				PartyIdentifier identifier = new PartyIdentifier();
				identifier.setType("Registration");
				identifier.setIdentifierNumber(registrationDetails.optString("registrationNumber"));
				identifier.setExpiryDate(registrationDetails.optString("expiryDate"));
				if (partyDTO.getPartyIdentifier() != null && partyDTO.getPartyIdentifier().size() > 0) {
					partyDTO.getPartyIdentifier().add(identifier);
				} else {
					List<PartyIdentifier> partyIdentifiers = new ArrayList<PartyIdentifier>();
					partyIdentifiers.add(identifier);
					partyDTO.setPartyIdentifier(partyIdentifiers);
				}
			}
        }

        if (map.get("userType") != null && StringUtils.isNotBlank((String) map.get("userType"))) {
            if (StringUtils.equalsAny(map.get("userType").toString(), "RelatedCompany", "Company")) {
                partyDTO.setPartyType("Organisation");
            }
        }
        return partyDTO;
    }

    public static PartyDTO buildPartyDTO(CustomerDTO customerDTO, PartyDTO partyDTO) {

        if (StringUtils.isNotBlank(customerDTO.getDateOfBirth())) {
            partyDTO.setDateOfBirth(customerDTO.getDateOfBirth());
        }

        boolean isProspect = false;

		partyDTO.setPartyStatus("Active");
		if (StringUtils.isNotBlank(customerDTO.getCustomerType_id())
				&& customerDTO.getCustomerType_id().equals(HelperMethods.getCustomerTypes().get("Prospect"))) {
			isProspect = true;
			partyDTO.setPartyStatus("Prospect");
		}

        partyDTO.setPartyType("Individual");

        if (StringUtils.isNotBlank(customerDTO.getFirstName())
                && !customerDTO.getFirstName().equals(partyDTO.getFirstName())) {
            partyDTO.setFirstName(customerDTO.getFirstName());
            partyDTO.setNameStartDate(HelperMethods.getCurrentDate());
        }

        if (StringUtils.isNotBlank(customerDTO.getLastName())
                && !customerDTO.getLastName().equals(partyDTO.getLastName())) {
            partyDTO.setLastName(customerDTO.getLastName());
            partyDTO.setNameStartDate(HelperMethods.getCurrentDate());
        }

        if (StringUtils.isNotBlank(customerDTO.getGender())) {
            partyDTO.setGender(StringUtils.capitalize(customerDTO.getGender().toLowerCase()));
        }
        
        if (StringUtils.isNotBlank(customerDTO.getTitle())) {
            partyDTO.setTitle(StringUtils.capitalize(customerDTO.getTitle().toLowerCase()));
        }

        if (customerDTO.getCustomerCommuncation() != null) {

            List<PartyAddress> contactPoints = partyDTO.getPartyAddress();
            PartyAddress contactPoint;
            Map<String,String> map = new HashMap<>();
            Map<String,String> temp = new HashMap<>();
            for (CustomerCommunicationDTO communicationDTO : customerDTO.getCustomerCommuncation()) {
                contactPoint = new PartyAddress();
                contactPoint.setLegalEntityId(communicationDTO.getCompanyLegalUnit());
                if (communicationDTO.getType_id()
                        .equals(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL))) {
                    boolean isNew = true;
                    if (contactPoints != null) {
                        for (int i = 0; i < contactPoints.size(); i++) {
                            if ((contactPoints.get(i).getCommunicationNature().equals(DTOConstants.ELECTRONIC)
                                    && contactPoints.get(i).getCommunicationType().equals(DTOConstants.EMAIL))
                                    && (StringUtils.isNotBlank(communicationDTO.getId())
                                            && StringUtils.isNotBlank(contactPoints.get(i).getAddressesReference())
                                            && communicationDTO.getId()
                                                    .equals(contactPoints.get(i).getAddressesReference()))
                                    || (Boolean.parseBoolean(contactPoints.get(i).getPrimary())
                                            && communicationDTO.getIsPrimary() && isProspect 
                                            && (contactPoints.get(i).getCommunicationNature()
													.equals(DTOConstants.ELECTRONIC)
													&& contactPoints.get(i).getCommunicationType()
															.equals(DTOConstants.EMAIL)))
                                    || communicationDTO.getValue()
                                            .equals(contactPoints.get(i).getElectronicAddress())) {
                                contactPoint = contactPoints.get(i);

                                contactPoint.setElectronicAddress(communicationDTO.getValue());

                                isNew = false;
                                break;
                            }
                        }
                    }

                    if (isNew) {
                        contactPoint.setCommunicationNature(DTOConstants.ELECTRONIC);
                        contactPoint.setCommunicationType(DTOConstants.EMAIL);
                        if (StringUtils.isBlank(contactPoint.getElectronicAddress())
                                || (StringUtils.isNotBlank(communicationDTO.getValue())
                                        && !communicationDTO.getValue()
                                                .equals(contactPoint.getElectronicAddress()))) {
                            contactPoint.setElectronicAddress(communicationDTO.getValue());
                        }
                        if (PartyMappings.getPartyPhoneAddressTypeMapping()
                                .containsKey(communicationDTO.getExtension())) {
                            contactPoint.setAddressType(PartyMappings.getPartyPhoneAddressTypeMapping()
                                    .get(communicationDTO.getExtension()));
                        } else {
                            contactPoint.setAddressType(communicationDTO.getExtension());
                        }
                        partyDTO.setPartyAddress(contactPoint);
                    }

                    if (communicationDTO.getIsPrimary()) {
                        removeAddressPrimary(contactPoints, contactPoint.getCommunicationNature());
                        contactPoint.setPrimary("true");
                    } else {
                        contactPoint.setPrimary("false");
                    }

                } else if (communicationDTO.getType_id()
                        .equals(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE))) {
                    boolean isNew = true;
                    String phone = communicationDTO.getValue();
                    String extenstion = communicationDTO.getPhoneCountryCode();
                    if (phone.contains("-")) {
                        extenstion = phone.substring(0, phone.indexOf('-'));
                        phone = phone.substring(extenstion.length() + 1);
                    }

                    if (contactPoints != null) {
                        for (int i = 0; i < contactPoints.size(); i++) {
                            if (contactPoints.get(i).getCommunicationNature().equals(DTOConstants.PHONE)
                                    && (StringUtils.isNotBlank(communicationDTO.getId())
                                            && StringUtils.isNotBlank(contactPoints.get(i).getAddressesReference())
                                            && communicationDTO.getId()
                                                    .equals(contactPoints.get(i).getAddressesReference()))
                                    || (Boolean.parseBoolean(contactPoints.get(i).getPrimary())
                                            && communicationDTO.getIsPrimary() && isProspect 
                                            && (contactPoints.get(i).getCommunicationNature().equals(DTOConstants.PHONE)))
                                    || communicationDTO.getValue().equals(
                                            contactPoints.get(i).getIddPrefixPhone() + "-"
                                                    + contactPoints.get(i).getPhoneNo())) {
                                contactPoint = contactPoints.get(i);
                                contactPoint.setPhoneNo(phone);
                                if (StringUtils.isNotBlank(extenstion)) {
                                    contactPoint.setIddPrefixPhone(extenstion);
                                }

                                isNew = false;
                                break;
                            }
                        }
                    }

                    if (isNew) {
                        contactPoint.setCommunicationNature(DTOConstants.PHONE);
                        contactPoint.setCommunicationType(PartyConstants.MOBILE);
                        if (PartyMappings.getPartyPhoneAddressTypeMapping()
                                .containsKey(communicationDTO.getExtension())) {
                            contactPoint.setAddressType(PartyMappings.getPartyPhoneAddressTypeMapping()
                                    .get(communicationDTO.getExtension()));
                        } else {
                            contactPoint.setAddressType(communicationDTO.getExtension());
                        }
                        contactPoint.setPhoneNo(phone);
                        contactPoint.setIddPrefixPhone(extenstion);
                        partyDTO.setPartyAddress(contactPoint);
                    }

                    if (communicationDTO.getIsPrimary()) {
                        removeAddressPrimary(contactPoints, contactPoint.getCommunicationNature());
                        contactPoint.setPrimary("true");
                    } else {
                        contactPoint.setPrimary("false");
					}
				}

				if (contactPoints != null) {
					for (int i = 0; i < contactPoints.size(); i++) {
						if (StringUtils.isNotBlank(contactPoints.get(i).getAddressesReference())
								&& communicationDTO.getValue().equals(contactPoints.get(i).getIddPrefixPhone() + "-"
										+ contactPoints.get(i).getPhoneNo())) {

							map.put(contactPoints.get(i).getAddressesReference(),
									contactPoints.get(i).getIddPrefixPhone() + "-" + contactPoints.get(i).getPhoneNo());

						} else if (StringUtils.isNotBlank(contactPoints.get(i).getAddressesReference())
								&& contactPoints.get(i).getCommunicationNature().equals(DTOConstants.PHONE)
								&& communicationDTO.getType_id()
		                        .equals(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE))) {

							temp.put(contactPoints.get(i).getAddressesReference(),
									contactPoints.get(i).getIddPrefixPhone() + "-" + contactPoints.get(i).getPhoneNo());

						}
						if (StringUtils.isNotBlank(contactPoints.get(i).getAddressesReference())
								&& communicationDTO.getValue().equals(contactPoints.get(i).getElectronicAddress())) {

							map.put(contactPoints.get(i).getAddressesReference(),
									contactPoints.get(i).getElectronicAddress());

						} else if (StringUtils.isNotBlank(contactPoints.get(i).getAddressesReference())
								&& contactPoints.get(i).getCommunicationType().equals(DTOConstants.EMAIL)
								&& communicationDTO.getType_id()
		                        .equals(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL))) {

							temp.put(contactPoints.get(i).getAddressesReference(),
									contactPoints.get(i).getElectronicAddress());

						}

					}
				}

			}
			Set<String> existing = new HashSet<>();
			map = map.entrySet().stream().filter(entry -> existing.add(entry.getValue()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			Iterator<String> itr = map.keySet().iterator();
			String key;
			while (itr.hasNext()) {
				key = itr.next();
				if (temp.containsKey(key)) {
					temp.remove(key);
				}
			}
			contactPoints = partyDTO.getPartyAddress();
			for (CustomerCommunicationDTO communicationDTO : customerDTO.getCustomerCommuncation()) {
				if (communicationDTO.getType_id().equals(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE))
						|| communicationDTO.getType_id()
								.equals(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL))) {
					if (contactPoints != null) {
						for (int i = 0; i < contactPoints.size(); i++) {
							if ((contactPoints.get(i).getCommunicationNature().equals(DTOConstants.PHONE)
									|| contactPoints.get(i).getCommunicationNature().equals(DTOConstants.ELECTRONIC))
									&& StringUtils.isNotBlank(contactPoints.get(i).getAddressesReference())
									&& temp.containsKey(contactPoints.get(i).getAddressesReference())) {
								Map<String, Object> headerMap = new HashMap<String, Object>();
								headerMap.put(DBPUtilitiesConstants.COMPANYID, communicationDTO.getCompanyLegalUnit());
								headerMap = PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
								String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
										+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_ADDRESSDELETE,
												partyDTO.getPartyId())
										+ contactPoints.get(i).getAddressesReference();

								HTTPOperations.sendHttpRequest(HTTPOperations.operations.DELETE, partyURL, null,
										headerMap);
								contactPoints.remove(i);
								i--;
							}

						}
					}

				}
			}

		}

        if (customerDTO.getEmploymentDetails() != null) {

            for (CustomerEmploymentDetailsDTO customerEmploymentDetailsDTO : customerDTO.getEmploymentDetails()) {

                List<Employment> employments = partyDTO.getEmployments();
                Employment employment = new Employment();

                boolean isNew = true;
                String employmentType;
                if ("SID_EMPLOYED".equals(customerEmploymentDetailsDTO.getEmploymentType())) {
                    employmentType = "Salaried-Employee";
                } else if ("SID_STUDENT".equals(customerEmploymentDetailsDTO.getEmploymentType())) {
                    employmentType = "Student";
                } else if ("SID_RETIRED".equals(customerEmploymentDetailsDTO.getEmploymentType())) {
                    employmentType = "Retired";
                } else if ("SID_UNEMPLOYED".equals(customerEmploymentDetailsDTO.getEmploymentType())) {
                    employmentType = "Individual";
                } else {
                    employmentType = "Self-Employed";
                }

                if (employments != null) {
                    for (int i = 0; i < employments.size(); i++) {
                        if (employments.get(i).getStartDate().equals(customerEmploymentDetailsDTO.getCreatedts())) {
                            employment = employments.get(i);
                            if (StringUtils.isBlank(employment.getType())
                                    || (StringUtils.isNotBlank(employment.getType())
                                            && !employment.getType().equals(employmentType))) {
                                employment.setType(employmentType);
                            }
                            isNew = false;
                            break;
                        }
                    }
                }

                if (isNew) {
                    employment.setType(employmentType);
                    employment.setStartDate(customerEmploymentDetailsDTO.getCreatedts());
                    partyDTO.setEmployment(employment);
                }
            }
        }

        if (customerDTO.getCustomerAddress() != null) {
        	
        	Map<String,String> map = new HashMap<>();
            Map<String,String> temp = new HashMap<>();
            List<PartyAddress> contactAddresss = partyDTO.getPartyAddress();
            PartyAddress contactAddress;
            
            for (CustomerAddressDTO customerAddressDTO : customerDTO.getCustomerAddress()) {
            	contactAddress = new PartyAddress();
                
                boolean isNew = true;

                if (contactAddresss != null) {
                    for (int i = 0; i < contactAddresss.size(); i++) {
                        if (contactAddresss.get(i).getCommunicationNature().equals(DTOConstants.PHYSICAL)
                                && StringUtils.isNotBlank(customerAddressDTO.getAddress_id())
                                && StringUtils.isNotBlank(contactAddresss.get(i).getAddressesReference())
                                && customerAddressDTO.getAddress_id()
                                        .equals(contactAddresss.get(i).getAddressesReference())) {
                            contactAddress = contactAddresss.get(i);
                            isNew = false;
                            break;
                        }
                    }
                }

                AddressDTO addressDTO = customerAddressDTO.getAddressDTO();

                if (StringUtils.isBlank(contactAddress.getBuildingName())
                        || (StringUtils.isNotBlank(contactAddress.getBuildingName())
                                && StringUtils.isNotBlank(addressDTO.getAddressLine1())
                                && !addressDTO.getAddressLine1().equals(contactAddress.getBuildingName()))) {
                    contactAddress.setBuildingName(addressDTO.getAddressLine1());
                }

                if (StringUtils.isBlank(contactAddress.getStreetName())
                        || (StringUtils.isNotBlank(contactAddress.getStreetName())
                                && StringUtils.isNotBlank(addressDTO.getAddressLine2())
                                && !addressDTO.getAddressLine2().equals(contactAddress.getStreetName()))) {
                    contactAddress.setStreetName(addressDTO.getAddressLine2());
                }
                if (StringUtils.isBlank(contactAddress.getTown()) || (StringUtils.isNotBlank(contactAddress.getTown())
                        && StringUtils.isNotBlank(addressDTO.getCityName())
                        && !addressDTO.getCityName().equals(contactAddress.getTown()))) {
                    contactAddress.setTown(addressDTO.getCityName());
                }

                if (StringUtils.isBlank(contactAddress.getCountrySubdivision())
                        || (StringUtils.isNotBlank(contactAddress.getCountrySubdivision())
                                && StringUtils.isNotBlank(addressDTO.getState())
                                && !addressDTO.getState().equals(contactAddress.getCountrySubdivision()))) {
                    contactAddress.setCountrySubdivision(addressDTO.getState());
                }

                if (StringUtils.isBlank(contactAddress.getTown()) || (StringUtils.isNotBlank(contactAddress.getTown())
                        && StringUtils.isNotBlank(addressDTO.getCity_id())
                        && !addressDTO.getCity_id().equals(contactAddress.getTown()))) {
                    contactAddress.setTown(addressDTO.getCity_id());
                }

                if (StringUtils.isBlank(contactAddress.getCountrySubdivision())
                        || (StringUtils.isNotBlank(contactAddress.getCountrySubdivision())
                                && StringUtils.isNotBlank(addressDTO.getRegion_id())
                                && !addressDTO.getRegion_id().equals(contactAddress.getCountrySubdivision()))) {
                    contactAddress.setCountrySubdivision(addressDTO.getRegion_id());
                }

                if (StringUtils.isBlank(contactAddress.getPostalOrZipCode())
                        || (StringUtils.isNotBlank(contactAddress.getPostalOrZipCode())
                                && StringUtils.isNotBlank(addressDTO.getZipCode())
                                && !addressDTO.getZipCode().equals(contactAddress.getPostalOrZipCode()))) {
                    contactAddress.setPostalOrZipCode(addressDTO.getZipCode());
                }

                if (StringUtils.isBlank(contactAddress.getCountryCode())
                        || !contactAddress.getCountryCode().equals(addressDTO.getCountry())) {
                    contactAddress.setCountryCode(addressDTO.getCountry());
                }

                contactAddress.setCommunicationNature(DTOConstants.PHYSICAL);
                contactAddress.setCommunicationType("MailingAddress");

                if (customerAddressDTO.getIsPrimary()) {
                    removeAddressPrimary(contactAddresss, contactAddress.getCommunicationNature());
                    contactAddress.setPrimary("true");
                } else {
                    contactAddress.setPrimary("false");
                }
                if (isNew) {
                    if (PartyMappings.getPartycontactAddressTypeMapping()
                            .containsKey(customerAddressDTO.getType_id())) {
                        contactAddress.setAddressType(
                                PartyMappings.getPartycontactAddressTypeMapping().get(customerAddressDTO.getType_id()));
                    } else {
                        contactAddress.setAddressType(customerAddressDTO.getType_id());
                    }
                    partyDTO.setPartyAddress(contactAddress);
                }
                
				String payloadAddress = addressDTO.getAddressLine1() + addressDTO.getAddressLine2()
						+ addressDTO.getCityName() + addressDTO.getRegion_id() + addressDTO.getZipCode()
						+ addressDTO.getCountry();
				contactAddresss = partyDTO.getPartyAddress();
				if (contactAddresss != null) {
					for (int i = 0; i < contactAddresss.size(); i++) {
						String fullContactAddress = contactAddresss.get(i).getBuildingName()
								+ contactAddresss.get(i).getStreetName() + contactAddresss.get(i).getTown()
								+ contactAddresss.get(i).getCountrySubdivision()
								+ contactAddresss.get(i).getPostalOrZipCode() + contactAddresss.get(i).getCountryCode();
						if (StringUtils.isNotBlank(contactAddresss.get(i).getAddressesReference())
								&& payloadAddress.equals(fullContactAddress) && 
								customerAddressDTO.getAddress_id()
                                .equals(contactAddresss.get(i).getAddressesReference())) {

							map.put(contactAddresss.get(i).getAddressesReference(), fullContactAddress);

						} else if (StringUtils.isNotBlank(contactAddresss.get(i).getAddressesReference())
								&& contactAddresss.get(i).getCommunicationNature().equals(DTOConstants.PHYSICAL)) {

							temp.put(contactAddresss.get(i).getAddressesReference(), fullContactAddress);

						}
					}
				}
			}
			Set<String> existing = new HashSet<>();
			map = map.entrySet().stream().filter(entry -> existing.add(entry.getValue()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			Iterator<String> itr = map.keySet().iterator();
			String key;
			while (itr.hasNext()) {
				key = itr.next();
				if (temp.containsKey(key)) {
					temp.remove(key);
				}
			}
			for (CustomerAddressDTO customerAddressDTO : customerDTO.getCustomerAddress()) {
				if (contactAddresss != null) {
					for (int i = 0; i < contactAddresss.size(); i++) {
						if (contactAddresss.get(i).getCommunicationNature().equals(DTOConstants.PHYSICAL)
								&& StringUtils.isNotBlank(contactAddresss.get(i).getAddressesReference())
								&& temp.containsKey(contactAddresss.get(i).getAddressesReference())) {
							Map<String, Object> headerMap = new HashMap<String, Object>();
							headerMap.put(DBPUtilitiesConstants.COMPANYID, customerAddressDTO.getCompanyLegalUnit());
							headerMap = PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
							String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
									+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_ADDRESSDELETE,
											partyDTO.getPartyId())
									+ contactAddresss.get(i).getAddressesReference();

							HTTPOperations.sendHttpRequest(HTTPOperations.operations.DELETE, partyURL, null, headerMap);
							contactAddresss.remove(i);
							i--;
						}

					}
				}

			}
		}

        if (StringUtils.isNotBlank(customerDTO.getiDType_id())) {
            List<PartyIdentifier> partyIdentifiers = new ArrayList<PartyIdentifier>();

            boolean isNew = true;
            PartyIdentifier partyIdentifier = new PartyIdentifier();

            if (partyDTO.getPartyIdentifier() != null) {
                partyIdentifiers = partyDTO.getPartyIdentifier();
                for (PartyIdentifier identifier : partyIdentifiers) {
                    if (identifier.getType()
                            .equals(PartyMappings.getPartyIdentifierMapping().get(customerDTO.getiDType_id()))) {
                        partyIdentifier = identifier;
                        isNew = false;
                        break;
                    }
                }
            }

            partyIdentifier.setExpiryDate(customerDTO.getiDExpiryDate());
            partyIdentifier.setIssuedDate(customerDTO.getiDIssueDate());

            partyIdentifier.setIssuingCountry(customerDTO.getiDCountry());

            partyIdentifier.setCountrySubdivision(customerDTO.getiDState());
            partyIdentifier.setIdentifierNumber(customerDTO.getiDValue());

            if (PartyMappings.getPartyIdentifierMapping().containsKey(customerDTO.getiDType_id())) {
                partyIdentifier.setType(PartyMappings.getPartyIdentifierMapping().get(customerDTO.getiDType_id()));
            } else {
                partyIdentifier.setType(customerDTO.getiDType_id());
            }

            if (isNew) {
                partyDTO.setPartyIdentifier(partyIdentifier);
            }

        }

        if (StringUtils.isNotBlank(customerDTO.getDrivingLicenseNumber())) {
            List<PartyIdentifier> partyIdentifiers = new ArrayList<PartyIdentifier>();

            boolean isNew = true;
            PartyIdentifier partyIdentifier = new PartyIdentifier();

            if (partyDTO.getPartyIdentifier() != null) {
                partyIdentifiers = partyDTO.getPartyIdentifier();
                for (PartyIdentifier identifier : partyIdentifiers) {
                    if (StringUtils.isNotBlank(identifier.getType()) && identifier.getType()
                            .equals(PartyMappings.getPartyIdentifierMapping().get("ID_DRIVING_LICENSE"))) {
                        partyIdentifier = identifier;
                        isNew = false;
                        break;
                    }
                }
            }

            partyIdentifier.setExpiryDate(customerDTO.getiDExpiryDate());
            partyIdentifier.setIssuedDate(customerDTO.getiDIssueDate());

            partyIdentifier.setIssuingCountry(customerDTO.getiDCountry());

            partyIdentifier.setCountrySubdivision(customerDTO.getiDState());
            partyIdentifier.setIdentifierNumber(customerDTO.getDrivingLicenseNumber());

            partyIdentifier.setType(PartyMappings.getPartyIdentifierMapping().get("ID_DRIVING_LICENSE"));

            if (isNew) {
                partyDTO.setPartyIdentifier(partyIdentifier);
            }

        }

        if (StringUtils.isNotBlank(customerDTO.getSsn())) {
            List<PartyIdentifier> partyIdentifiers = new ArrayList<PartyIdentifier>();

            boolean isNew = true;
            PartyIdentifier partyIdentifier = new PartyIdentifier();

            if (partyDTO.getPartyIdentifier() != null) {
                partyIdentifiers = partyDTO.getPartyIdentifier();
                for (PartyIdentifier identifier : partyIdentifiers) {
                    if (StringUtils.isNotBlank(identifier.getType())
                            && identifier.getType().equals(DTOConstants.PARTY_CREATE_SSN)) {
                        partyIdentifier = identifier;
                        isNew = false;
                        break;
                    }
                }
            }

            partyIdentifier.setIdentifierNumber(customerDTO.getSsn());

            partyIdentifier.setType(DTOConstants.PARTY_CREATE_SSN);

            if (isNew) {
                partyDTO.setPartyIdentifier(partyIdentifier);
            }

        }

        if (StringUtils.isNotBlank(customerDTO.getTaxID())) {

            List<TaxDetails> details = partyDTO.getTaxDetails();
            boolean isNew = true;
            if (details != null) {
                for (TaxDetails detail : details) {
                    if (detail.getTaxType().equals(DTOConstants.INCOME_TAX)) {
                        detail.setTaxId(customerDTO.getTaxID());
                        isNew = false;
                        break;
                    }
                }
            }

            if (isNew) {
                TaxDetails taxdetails = new TaxDetails();
                taxdetails.setTaxType(DTOConstants.INCOME_TAX);
                taxdetails.setTaxId(customerDTO.getTaxID());

                partyDTO.setTaxDetails(taxdetails);
            }
        }

        return partyDTO;
    }

    private static boolean removeAddressPrimary(List<PartyAddress> contactAddresss, String communicationNature) {
        boolean status = false;
        if (contactAddresss != null) {
            for (PartyAddress address : contactAddresss) {
                if (address.getCommunicationNature().equals(communicationNature)) {
                    address.setPrimary("false");
                    status = true;
                }
            }
        }

        return status;
    }

    public static void updateCustomerDTOFromPartyDTO(CustomerDTO customerDTO, PartyDTO partyDTO) {
        initLOG();
        customerDTO.setCreatedts(null);
        if (StringUtils.isBlank(customerDTO.getDateOfBirth()) || (StringUtils.isNotBlank(partyDTO.getDateOfBirth())
                && !customerDTO.getDateOfBirth().equals(partyDTO.getDateOfBirth()))) {
            customerDTO.setIsChanged(true);
            customerDTO.setDateOfBirth(HelperMethods.getFormattedTimeStamp(
                    HelperMethods.getFormattedTimeStamp(partyDTO.getDateOfBirth()), "yyyy-MM-dd"));
        }

        if (StringUtils.isNotBlank(partyDTO.getPartyStatus()) && partyDTO.getPartyStatus().equals("Active")) {
            if (StringUtils.isBlank(customerDTO.getCustomerType_id())
                    || !customerDTO.getCustomerType_id().equals(HelperMethods.getCustomerTypes().get("Customer"))) {
                customerDTO.setIsChanged(true);
            }

            if (StringUtils.isNotBlank(customerDTO.getPassword())
                    || !customerDTO.getStatus_id().equals("SID_CUS_NEW")) {
                customerDTO.setIsChanged(true);
                customerDTO.setStatus_id("SID_CUS_ACTIVE");
            }
            customerDTO.setCustomerType_id(HelperMethods.getCustomerTypes().get("Customer"));
        } else {

            if (StringUtils.isBlank(customerDTO.getCustomerType_id())
                    || !customerDTO.getCustomerType_id().equals(HelperMethods.getCustomerTypes().get("Prospect"))) {
                customerDTO.setIsChanged(true);
            }

            customerDTO.setStatus_id("SID_CUS_NEW");
			if (StringUtils.isNotBlank(partyDTO.getPartyStatus()))
				customerDTO.setCustomerType_id(HelperMethods.getCustomerTypes().get("Prospect"));
			else
				customerDTO.setCustomerType_id(HelperMethods.getCustomerTypes().get("Customer"));
        }

        if (StringUtils.isBlank(customerDTO.getFirstName()) || (StringUtils.isNotBlank(customerDTO.getFirstName())
                && StringUtils.isNotBlank(partyDTO.getFirstName())
                && !customerDTO.getFirstName().equals(partyDTO.getFirstName()))) {
            customerDTO.setIsChanged(true);
            customerDTO.setFirstName(partyDTO.getFirstName());
        }

        if (StringUtils.isBlank(customerDTO.getLastName())
                || (StringUtils.isNotBlank(customerDTO.getLastName()) && StringUtils.isNotBlank(partyDTO.getLastName())
                        && !customerDTO.getLastName().equals(partyDTO.getLastName()))) {
            customerDTO.setIsChanged(true);
            customerDTO.setLastName(partyDTO.getLastName());
        }
        
        Map<String,Object> extensionData = partyDTO.getExtensionData();
        
		if (extensionData != null && !extensionData.isEmpty()) {
			if (extensionData.get("sbaEnrolmentStatus") != null) {
				customerDTO.setSbaEnrolmentStatus(extensionData.get("sbaEnrolmentStatus").toString());
			}
		}

        List<PartyAddress> contactPoints = partyDTO.getPartyAddress();
        if (contactPoints != null) {
            CustomerCommunicationDTO communicationDTO = null;
            List<CustomerCommunicationDTO> list = customerDTO.getCustomerCommuncation();

            for (PartyAddress contactPoint : contactPoints) {
                communicationDTO = new CustomerCommunicationDTO();
                if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC) &&
                        contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)) {
                    boolean isNew = true;
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (communicationDTO.getType_id().equals(HelperMethods.getCommunicationTypes().get("Email"))
                                    &&
                                    StringUtils.isNotBlank(list.get(i).getId())
                                    && StringUtils.isNotBlank(contactPoint.getAddressesReference())
                                    && list.get(i).getId().equals(contactPoint.getAddressesReference())) {

                                communicationDTO = list.get(i);
                                if (StringUtils.isBlank(contactPoint.getElectronicAddress()) || (StringUtils
                                        .isNotBlank(communicationDTO.getValue())
                                        && !communicationDTO.getValue().equals(contactPoint.getElectronicAddress()))) {
                                    communicationDTO.setValue(contactPoint.getElectronicAddress());
                                    communicationDTO.setIsChanged(true);
                                    if (Boolean.parseBoolean(contactPoint.getPrimary())) {
                                        removePrimary(list, communicationDTO.getType_id());
                                        communicationDTO.setIsPrimary(true);
                                    }
                                }
                                isNew = false;
                                break;
                            }
                        }
                    }

                    if (isNew) {
                        communicationDTO.setIsNew(isNew);

                        if (Boolean.parseBoolean(contactPoint.getPrimary())) {
                            removePrimary(list, communicationDTO.getType_id());
                            communicationDTO.setIsPrimary(true);
                        } else {
                            communicationDTO.setIsPrimary(false);
                        }
                        communicationDTO.setExtension(contactPoint.getAddressType());
                        communicationDTO.setId(contactPoint.getAddressesReference());
                        communicationDTO.setCustomer_id(customerDTO.getId());
                        communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));
                        communicationDTO.setValue(contactPoint.getElectronicAddress());
                        communicationDTO.setId(contactPoint.getAddressesReference());
                        customerDTO.setCustomerCommuncation(communicationDTO);
                    }
                } else if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)) {
                    boolean isNew = true;
                    String phone = contactPoint.getPhoneNo();
                    String extention = contactPoint.getIddPrefixPhone();
                    if (StringUtils.isNotBlank(extention)) {
                        phone = extention + "-" + phone;
                    }
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {

                            if (communicationDTO.getType_id().equals(HelperMethods.getCommunicationTypes().get("Phone"))
                                    &&
                                    StringUtils.isNotBlank(list.get(i).getId())
                                    && StringUtils.isNotBlank(contactPoint.getAddressesReference())
                                    && list.get(i).getId().equals(contactPoint.getAddressesReference())) {
                                communicationDTO = list.get(i);
                                communicationDTO.setValue(phone);
                                communicationDTO.setPhoneCountryCode(extention);
                                communicationDTO.setIsChanged(true);
                                if (Boolean.parseBoolean(contactPoint.getPrimary())) {
                                    removePrimary(list, communicationDTO.getType_id());
                                    communicationDTO.setIsPrimary(true);
                                }

                                isNew = false;
                                break;
                            }
                        }
                    }

                    if (isNew) {
                        communicationDTO.setIsNew(isNew);
                        if (Boolean.parseBoolean(contactPoint.getPrimary())) {
                            removePrimary(list, communicationDTO.getType_id());
                            communicationDTO.setIsPrimary(true);
                        } else {
                            communicationDTO.setIsPrimary(false);
                        }

                        communicationDTO.setId(contactPoint.getAddressesReference());
                        communicationDTO.setExtension(
                                PartyMappings.getCustomerPhoneTypeMapping().get(contactPoint.getCommunicationType()));
                        communicationDTO.setId(contactPoint.getAddressesReference());
                        communicationDTO.setCustomer_id(customerDTO.getId());
                        communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));
                        communicationDTO.setValue(phone);
                        communicationDTO.setPhoneCountryCode(extention);
                        customerDTO.setCustomerCommuncation(communicationDTO);
                    }
                }
            }
        }

        if (partyDTO.getEmployments() != null) {

            for (Employment employment : partyDTO.getEmployments()) {

                List<CustomerEmploymentDetailsDTO> customerEmploymentDetailsDTOs = customerDTO.getEmploymentDetails();
                CustomerEmploymentDetailsDTO customerEmploymentDetailsDTO = new CustomerEmploymentDetailsDTO();

                boolean isNew = true;

                if (customerEmploymentDetailsDTOs != null) {
                    for (int i = 0; i < customerEmploymentDetailsDTOs.size(); i++) {
                        if (customerEmploymentDetailsDTOs.get(i).getCreatedts().equals(employment.getStartDate())) {
                            customerEmploymentDetailsDTO = customerEmploymentDetailsDTOs.get(i);
                            isNew = false;
                            break;
                        }
                    }
                }

                if ("Salaried-Employee".equals(employment.getType())) {
                    customerEmploymentDetailsDTO.setEmploymentType("SID_EMPLOYED");
                } else if ("Student".equals(employment.getType())) {
                    customerEmploymentDetailsDTO.setEmploymentType("SID_STUDENT");
                } else if ("Retired".equals(employment.getType())) {
                    customerEmploymentDetailsDTO.setEmploymentType("SID_RETIRED");
                } else {
                    customerEmploymentDetailsDTO.setEmploymentType("SID_UNEMPLOYED");
                }

                if (StringUtils.isNotBlank(employment.getEmployerName())) {
                    customerEmploymentDetailsDTO.setCurrentEmployer(employment.getEmployerName());
                }

                if (StringUtils.isNotBlank(employment.getJobTitle())) {
                    customerEmploymentDetailsDTO.setDesignation(employment.getJobTitle());
                }

                if (StringUtils.isNotBlank(employment.getSalary() + "")) {
                    customerEmploymentDetailsDTO.setGrossIncome("" + employment.getSalary());
                }

                if (StringUtils.isNotBlank(employment.getEmployerName())) {
                    customerEmploymentDetailsDTO.setCurrentEmployer(employment.getEmployerName());
                }

                if (StringUtils.isNotBlank(employment.getEmployerName())) {
                    customerEmploymentDetailsDTO.setCurrentEmployer(employment.getEmployerName());
                }

                customerEmploymentDetailsDTO.setIsChanged(true);

                if (isNew) {
                    customerEmploymentDetailsDTO.setIsNew(true);
                    customerEmploymentDetailsDTO.setId(HelperMethods.getNewId());
                    customerEmploymentDetailsDTO.setCustomer_id(customerDTO.getId());
                    customerEmploymentDetailsDTO.setCreatedts(employment.getStartDate());
                    customerDTO.setEmploymentDetails(customerEmploymentDetailsDTO);
                }
            }
        }

        if (partyDTO.getPartyAddress() != null) {

            for (PartyAddress contactAddress : partyDTO.getPartyAddress()) {

                if (contactAddress.getCommunicationNature().equals(DTOConstants.PHYSICAL)) {
                    List<CustomerAddressDTO> customerAddressDTOs = customerDTO.getCustomerAddress();
                    CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO();
                    AddressDTO addressDTO = new AddressDTO();
                    addressDTO.setId(HelperMethods.getNewId());
                    customerAddressDTO.setAddress_id(addressDTO.getId());
                    customerAddressDTO.setCustomer_id(customerDTO.getId());
                    boolean isNew = true;

                    if (customerAddressDTOs != null) {
                        for (int i = 0; i < customerAddressDTOs.size(); i++) {
                            if (StringUtils.isNotBlank(customerAddressDTOs.get(i).getAddress_id()) &&
                                    StringUtils.isNotBlank(contactAddress.getAddressesReference())
                                    && customerAddressDTOs.get(i).getAddress_id()
                                            .equals(contactAddress.getAddressesReference())) {
                                customerAddressDTO = customerAddressDTOs.get(i);
                                addressDTO = customerAddressDTO.getAddressDTO();
                                if (Boolean.parseBoolean(contactAddress.getPrimary())) {
                                    removePrimary(customerAddressDTOs);
                                    customerAddressDTO.setIsPrimary(true);
                                }
                                isNew = false;
                                break;
                            }
                        }
                    }

                    customerAddressDTO.setType_id(contactAddress.getCommunicationType());

                    if (StringUtils.isNotBlank(contactAddress.getBuildingName())) {
                        addressDTO.setAddressLine1(contactAddress.getBuildingName());
                    }

                    if (StringUtils.isNotBlank(contactAddress.getStreetName())) {
                        addressDTO.setAddressLine2(contactAddress.getStreetName());
                    }

                    if (StringUtils.isNotBlank(contactAddress.getTown())) {
                        addressDTO.setCityName(contactAddress.getTown());
                    }

                    if (StringUtils.isNotBlank(contactAddress.getCountrySubdivision())) {
                        addressDTO.setState(contactAddress.getCountrySubdivision());
                    }
                    if (StringUtils.isNotBlank(contactAddress.getCountryCode())) {
                        addressDTO.setCountry(contactAddress.getCountryCode());
                    }

                    if (StringUtils.isNotBlank(contactAddress.getPostalOrZipCode())) {
                        addressDTO.setZipCode(contactAddress.getPostalOrZipCode());
                    }

                    customerAddressDTO.setIsChanged(true);
                    addressDTO.setIsChanged(true);

                    if (isNew) {
                        if (Boolean.parseBoolean(contactAddress.getPrimary())) {
                            removePrimary(customerAddressDTOs);
                            customerAddressDTO.setIsPrimary(true);
                        }
                        customerAddressDTO.setAddress_id(contactAddress.getAddressesReference());
                        addressDTO.setId(contactAddress.getAddressesReference());
                        customerAddressDTO.setIsNew(isNew);
                        addressDTO.setIsNew(isNew);
                        customerAddressDTO.setAddressDTO(addressDTO);
                        customerDTO.setCustomerAddress(customerAddressDTO);
                    }
                }
            }
        }

        if (partyDTO.getPartyIdentifier() != null) {

            for (PartyIdentifier identifier : partyDTO.getPartyIdentifier()) {

                if (!identifier.getType().equals(DTOConstants.SOCIAL_SECURITY_NUMBER) && !identifier.getType().equals(DTOConstants.PARTY_CREATE_SSN)) {
                    customerDTO.setiDType_id(HelperMethods.getCustomerIdentifierMapping().get(identifier.getType()));
                    customerDTO.setiDExpiryDate(identifier.getExpiryDate());
                    customerDTO.setiDIssueDate(identifier.getIssuedDate());
                    customerDTO.setiDCountry(identifier.getIssuingCountry());
                    customerDTO.setiDState(identifier.getCountrySubdivision());
                    customerDTO.setiDValue(identifier.getIdentifierNumber());
                } else if (identifier.getType().equals(DTOConstants.SOCIAL_SECURITY_NUMBER)) {
                    customerDTO.setSsn(identifier.getIdentifierNumber());
                }

                if (identifier.getType().equals(DTOConstants.DRIVER_LICENSE)) {
                    customerDTO.setDrivingLicenseNumber(identifier.getIdentifierNumber());
                }
            }
        }

        if (partyDTO.getAlternateIdentities() != null) {
            for (AlternateIdentity alternateIdentity : partyDTO.getAlternateIdentities()) {
                if (alternateIdentity.getIdentityType().equalsIgnoreCase("SalesForce")) {
                    customerDTO.setSalesForceIdentifier(alternateIdentity.getIdentityNumber());
                    break;
                }
            }

        }

    }

    private static Map<String, Integer> emplymentTypeMap;
    static {
        emplymentTypeMap = new HashMap<String, Integer>();
        emplymentTypeMap.put("SID_EMPLOYED", 1);
        emplymentTypeMap.put("SID_RETIRED", 5);
        emplymentTypeMap.put("SID_STUDENT", 3);
        emplymentTypeMap.put("SID_UNEMPLOYED", 4);
    }

    private static Map<Integer, String> emplymentType;
    static {
        emplymentType = new HashMap<Integer, String>();
        emplymentType.put(1, "SID_EMPLOYED");
        emplymentType.put(5, "SID_RETIRED");
        emplymentType.put(3, "SID_STUDENT");
        emplymentType.put(4, "SID_UNEMPLOYED");
    }

    public static Map<String, Object> addJWTAuthHeader(DataControllerRequest request, Map<String, Object> headers,
            String flow_type) {
    	try {
    	request.addRequestParam_("flowType", flow_type);
        String authToken = TokenUtils.getPartyMSAuthToken(request);
        headers.put("Authorization", authToken);

        String deploymentPlatform =
                URLFinder.getServerRuntimeProperty(PartyPropertyConstants.PARTYMS_DEPLOYMENT_PLATFORM);
        if (StringUtils.isNotBlank(deploymentPlatform)) {
            if (StringUtils.equals(deploymentPlatform, PartyPropertyConstants.AWS))
                headers.put("x-api-key",
                        URLFinder.getServerRuntimeProperty(PartyPropertyConstants.PARTYMS_AUTHORIZATION_KEY));
            if (StringUtils.equals(deploymentPlatform, PartyPropertyConstants.AZURE))
                headers.put("x-functions-key",
                        URLFinder.getServerRuntimeProperty(PartyPropertyConstants.PARTYMS_AUTHORIZATION_KEY));
        }
        LegalEntityUtil.addCompanyIDToHeaders(request);
        return headers;
    	} catch(Exception ex) {
    		return headers;
    	}
    }

    public static Map<String, Object> addJWTAuthHeader(Map<String, Object> headers,
            String flow_type) {
    	try {
        String authToken = TokenUtils.getCommonMSAuthToken();
        headers.put("Authorization", authToken);

        String deploymentPlatform =
                URLFinder.getServerRuntimeProperty(PartyPropertyConstants.PARTYMS_DEPLOYMENT_PLATFORM);
        if (StringUtils.isNotBlank(deploymentPlatform)) {
            if (StringUtils.equals(deploymentPlatform, PartyPropertyConstants.AWS))
                headers.put("x-api-key",
                        URLFinder.getServerRuntimeProperty(PartyPropertyConstants.PARTYMS_AUTHORIZATION_KEY));
            if (StringUtils.equals(deploymentPlatform, PartyPropertyConstants.AZURE))
                headers.put("x-functions-key",
                        URLFinder.getServerRuntimeProperty(PartyPropertyConstants.PARTYMS_AUTHORIZATION_KEY));
        }
    	}catch(Exception e){
    		return headers;
    	}
        return headers;
    }

    public static PartyDetails buildPartyDTOforEmailPhone(CustomerDTO customerDTO) {

        PartyDetails partyDetails = new PartyDetails();

        List<CustomerCommunicationDTO> customerCommuncationList = null;
        customerCommuncationList = customerDTO.getCustomerCommuncation();

        CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();

        customerCommunicationDTO = customerCommuncationList.get(customerCommuncationList.size() - 1);

        partyDetails.setPrimary(
                customerCommunicationDTO.getIsPrimary() != null ? customerCommunicationDTO.getIsPrimary().toString()
                        : "");

        if (customerDTO.getDetailToBeUpdated()
                .contentEquals(PartyProfileDetailsConstants.PARAM_DETAILTOBEUPDATED_EMAIL)) {

            partyDetails.setCommunicationNature(PartyProfileDetailsConstants.PARAM_COMMUNICATION_NATURE_EMAIL);
            partyDetails.setCommunicationType(PartyProfileDetailsConstants.PARAM_COMMUNICATION_TYPE_EMAIL);
            partyDetails.setAddressType(PartyProfileDetailsConstants.PARAM_ADDRESS_TYPE_EMAIL);
            partyDetails.setElectronicAddress(
                    customerCommunicationDTO.getValue() != null ? customerCommunicationDTO.getValue() : "");

            if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_UPDATE)) {
                partyDetails.setAddressesReference(
                        customerCommunicationDTO.getId() != null ? customerCommunicationDTO.getId()
                                : "");
            }
        } else if (customerDTO.getDetailToBeUpdated()
                .contentEquals(PartyProfileDetailsConstants.PARAM_DETAILTOBEUPDATED_PHONE)) {
            partyDetails.setCommunicationNature(PartyProfileDetailsConstants.PARAM_COMMUNICATION_NATURE_PHONE);
            partyDetails.setCommunicationType(PartyProfileDetailsConstants.PARAM_COMMUNICATION_TYPE_PHONE);

            String Extension =
                    customerCommunicationDTO.getExtension() != null ? customerCommunicationDTO.getExtension() : "";

            if (StringUtils.isNotBlank(Extension)) {
                if (Extension.contentEquals("Home")) {
                    partyDetails.setAddressType("Home");
                } else if (Extension.contentEquals("Work")) {
                    partyDetails.setAddressType("Office");
                } else if (Extension.contentEquals("Mobile")) {
                    partyDetails.setAddressType("Residence");
                } else if (Extension.contentEquals("Other")) {
                    partyDetails.setAddressType("Other");
                }
            }

            partyDetails.setPhoneNo(
                    customerCommunicationDTO.getValue() != null ? customerCommunicationDTO.getValue() : "");

            if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_UPDATE)) {
                partyDetails.setAddressesReference(
                        customerCommunicationDTO.getId() != null ? customerCommunicationDTO.getId()
                                : "");
            }
        }

        return partyDetails;
    }

    public static PartyDetails buildPartyDTOforAddress(CustomerDTO customerDTO, String partyId,
            Map<String, Object> headerMap) {

        PartyDetails partyDetails = new PartyDetails();
        CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO();

        List<CustomerAddressDTO> customerAddressList = null;

        if (customerDTO.getCustomerAddress() != null) {
            customerAddressList = customerDTO.getCustomerAddress();
        }

        customerAddressDTO = customerAddressList.get(customerAddressList.size() - 1);
        AddressDTO addressDTO = customerAddressDTO.getAddressDTO();

        partyDetails.setCommunicationNature(PartyProfileDetailsConstants.PARAM_COMMUNICATION_NATURE_ADDRESS);
        partyDetails.setCommunicationType(PartyProfileDetailsConstants.PARAM_COMMUNICATION_TYPE_ADDRESS);
        partyDetails.setPrimary(customerAddressDTO.getIsPrimary().toString());

        String addressType = customerAddressDTO.getType_id() != null ? customerAddressDTO.getType_id() : "";

        if (PartyMappings.getPartycontactAddressTypeMapping()
                .containsKey(addressType)) {
            partyDetails.setAddressType(PartyMappings.getPartycontactAddressTypeMapping()
                    .get(addressType));
        }

        if (StringUtils.isNotBlank(addressDTO.getAddressLine1())) {
            partyDetails.setBuildingName(addressDTO.getAddressLine1());
        }
        if (StringUtils.isNotBlank(addressDTO.getAddressLine2())) {
            partyDetails.setStreetName(addressDTO.getAddressLine2());
        }
        if (StringUtils.isNotBlank(addressDTO.getCity_id())) {
            partyDetails.setTown(addressDTO.getCity_id());
        }
        if (StringUtils.isNotBlank(addressDTO.getRegion_id())) {
            partyDetails.setRegionCode(addressDTO.getRegion_id());
            if (addressDTO.getRegion_id().contains("-")) {
                String[] parts = addressDTO.getRegion_id().split("-");
                partyDetails.setCountryCode(parts[0]);
                partyDetails.setCountrySubdivision(parts[1]);
            }
        } else if (StringUtils.isBlank(addressDTO.getRegion_id())) {
            partyDetails.setCountryCode(addressDTO.getCountry());
        }
        if (StringUtils.isNotBlank(addressDTO.getZipCode())) {
            partyDetails.setPostalOrZipCode(addressDTO.getZipCode());
        }

        if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_UPDATEADDRESS)) {
            if (StringUtils.isNotBlank(addressDTO.getId())) {
                partyDetails.setAddressesReference(addressDTO.getId());
            }
        }
        return partyDetails;
    }

    private static void removePrimary(List<CustomerCommunicationDTO> customerCommuncation, String type_id) {
        if (customerCommuncation != null && customerCommuncation.size() > 0) {
            for (CustomerCommunicationDTO communicationDTO : customerCommuncation) {
                if (type_id.equals(communicationDTO.getType_id()) && communicationDTO.getIsPrimary()) {
                    communicationDTO.setIsPrimary(false);
                    communicationDTO.setIsChanged(true);
                }
            }
        }
    }

    private static void removePrimary(List<CustomerAddressDTO> customerAddressDTOs) {
        if (customerAddressDTOs != null) {
            for (CustomerAddressDTO customerAddressDTO : customerAddressDTOs) {
                if (customerAddressDTO.getIsPrimary()) {
                    customerAddressDTO.setIsPrimary(false);
                    customerAddressDTO.setIsChanged(true);
                }
            }
        }
    }

    public static PartyDTO buildPartyfromInput(Map<String, String> inputMap, JsonObject jsonObject) {
        PartyDTO dto = new PartyDTO();
        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }
        boolean isCreateFlow = true;

        if (StringUtils.isNotBlank(inputMap.get(PartyConstants.partyId))) {
            jsonObject.addProperty(PartyConstants.partyId, inputMap.get(PartyConstants.partyId));
            isCreateFlow = false;
        }

        Set<Entry<String, String>> entry = inputMap.entrySet();

        for (Entry<String, String> e : entry) {
            if (StringUtils.isNotBlank(e.getValue())) {
                jsonObject.addProperty(e.getKey(), e.getValue());
            }
        }

        JsonArray addresses =
                jsonObject.has(PartyConstants.addresses) && jsonObject.get(PartyConstants.addresses).isJsonArray()
                        ? jsonObject.get(PartyConstants.addresses).getAsJsonArray()
                        : new JsonArray();
        JsonArray contactAddress = null;
        if (StringUtils.isNotBlank(inputMap.get(PartyConstants.contactAddress))) {
            try {
                contactAddress = new JsonParser().parse(inputMap.get(PartyConstants.contactAddress)).getAsJsonArray();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        JsonArray electronicAddress = null;
        if (StringUtils.isNotBlank(inputMap.get(PartyConstants.electronicAddress))) {
            try {
                electronicAddress =
                        new JsonParser().parse(inputMap.get(PartyConstants.electronicAddress)).getAsJsonArray();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
  //adding party relations*********************
        JsonArray partyRelations = null;
        if (StringUtils.isNotBlank(inputMap.get(PartyConstants.partyRelations))) {
            try {
            	partyRelations =
                        new JsonParser().parse(inputMap.get(PartyConstants.partyRelations)).getAsJsonArray();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

//********************************
        JsonArray phoneAddress = null;
        if (StringUtils.isNotBlank(inputMap.get(PartyConstants.phoneAddress))) {
            try {
                phoneAddress = new JsonParser().parse(inputMap.get(PartyConstants.phoneAddress)).getAsJsonArray();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        dto.loadFromJson(jsonObject);

        if (contactAddress != null) {
            PartyAddress address = new PartyAddress();
            for(JsonElement element : contactAddress) {
                address.loadFromJson(element.getAsJsonObject());
                if ((isCreateFlow) || (Boolean.parseBoolean(address.getPrimary())
                        && removeAddressPrimary(dto.getPartyAddress(), DTOConstants.PHYSICAL))) {
                    address.setPrimary("true");
                }
                if (StringUtils.isNotBlank(address.getAddressesReference())
                        && removeAddress(dto.getPartyAddress(), address.getAddressesReference(), DTOConstants.PHYSICAL)) {
                    PartyAddress oldAddress = updateAddress(addresses, element.getAsJsonObject());
                    dto.setPartyAddress(oldAddress);
                } else {
                    address.setCommunicationNature(DTOConstants.PHYSICAL);
                    address.setCommunicationType(PartyConstants.MailingAddress);
                    if (StringUtils.isBlank(address.getAddressType())) {
                        address.setAddressType(PartyConstants.Home);
                    }
                    dto.setPartyAddress(address);
                }
            }
        }

        if (phoneAddress != null) {
            PartyAddress address = new PartyAddress();
            for(JsonElement element : phoneAddress) {
                address.loadFromJson(element.getAsJsonObject());
                if ((isCreateFlow) || (Boolean.parseBoolean(address.getPrimary())
                        && removeAddressPrimary(dto.getPartyAddress(), DTOConstants.PHONE))) {
                    address.setPrimary("true");
                }
                if (StringUtils.isNotBlank(address.getAddressesReference())
                        && removeAddress(dto.getPartyAddress(), address.getAddressesReference(), DTOConstants.PHONE)) {
                    PartyAddress oldAddress = updateAddress(addresses, element.getAsJsonObject());
                    dto.setPartyAddress(oldAddress);
                } else {
                    address.setCommunicationNature(DTOConstants.PHONE);
                    address.setCommunicationType(PartyConstants.MOBILE);
                    if (StringUtils.isBlank(address.getAddressType())) {
                        address.setAddressType(PartyConstants.Home);
                    }
                    dto.setPartyAddress(address);
                }
            }
        }

        if (electronicAddress != null) {
            PartyAddress address = new PartyAddress();
            for(JsonElement element : electronicAddress) {
                address.loadFromJson(element.getAsJsonObject());
                if ((isCreateFlow) || (Boolean.parseBoolean(address.getPrimary())
                        && removeAddressPrimary(dto.getPartyAddress(), DTOConstants.ELECTRONIC))) {
                    address.setPrimary("true");
                }
                if (StringUtils.isNotBlank(address.getAddressesReference())
                        && removeAddress(dto.getPartyAddress(), address.getAddressesReference(), DTOConstants.ELECTRONIC)) {
                    PartyAddress oldAddress = updateAddress(addresses, element.getAsJsonObject());
                    dto.setPartyAddress(oldAddress);
                } else {
                    address.setCommunicationNature(DTOConstants.ELECTRONIC);
                    address.setCommunicationType(PartyConstants.EMAIL);
                    if (StringUtils.isBlank(address.getAddressType())) {
                        address.setAddressType(PartyConstants.Home);
                    }
                    dto.setPartyAddress(address);
                }
            }
        }
		Map<String, Object> sbaStatus = new HashMap<>();
		sbaStatus.put("sbaEnrolmentStatus", inputMap.get("sbaEnrolmentStatus"));
		dto.setExtensionData(sbaStatus);
		return dto;
    }

    private static PartyAddress updateAddress(JsonArray addresses, JsonObject address) {
        PartyAddress oldAddress = new PartyAddress();
        if (addresses == null) {
            oldAddress.loadFromJson(address);
        } else {
            for (int i = 0; i < addresses.size(); i++) {
                JsonObject oldObject = addresses.get(i).getAsJsonObject();
                if (address.get(PartyConstants.addressesReference).getAsString()
                        .equals(oldObject.get(PartyConstants.addressesReference).getAsString()) &&
                        address.get(PartyConstants.communicationNature).getAsString()
                                .equals(oldObject.get(PartyConstants.communicationNature).getAsString())) {
                    for (Entry<String, JsonElement> entry : address.entrySet()) {
                        if (!entry.getValue().isJsonNull() && StringUtils.isNotBlank(entry.getValue().getAsString())) {
                            oldObject.add(entry.getKey(), entry.getValue());
                        }
                    }

                    oldAddress.loadFromJson(oldObject);
                    return oldAddress;
                }
            }
        }

        return oldAddress;
    }

    private static boolean removeAddress(List<PartyAddress> addresses, String addressesReference, String nature) {
        if (addresses == null)
            return false;
        for (int i = 0; i < addresses.size(); i++) {
            if (addressesReference.equals(addresses.get(i).getAddressesReference())
                    && nature.equals(addresses.get(i).getCommunicationNature())) {
                addresses.remove(i);
                return true;
            }
        }

        return false;
    }

    public static boolean allowPhoneEmailAddressCreation(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        LoggerUtil logger = new LoggerUtil(PartyUtils.class);

        int phoneCount = 0;
        int emailCount = 0;
        int addressCount = 0;

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerDTO.getId());
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
        backendIdentifierDTO.setCompanyLegalUnit(customerDTO.getCompanyLegalUnit());
        DBXResult backendResult = new DBXResult();

        try {
            backendResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
        } catch (ApplicationException e) {
            alert.prepareError("exception while fetching Backend Identifier", e).log();
        }
        String partyId = null;

        try {
            if (backendResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) backendResult.getResponse();

                partyId = identifierDTO.getBackendId();
            }

            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
                    + PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_COMMUNICATION, partyId);
            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
            DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null,
                    headerMap);
            
            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
            if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATEADDRESS)) {
                if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
                        && !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
                    List<PartyAddress> contactAddresses = PartyAddress
                            .loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
                    for (PartyAddress contactAddress : contactAddresses) {
                        if (contactAddress.getCommunicationNature() != null
                                && contactAddress.getCommunicationNature().equals(DTOConstants.PHYSICAL)) {
                            addressCount++;
                        }
                    }
                }
            } else {
                if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
                        && !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
                    List<PartyAddress> contactPoints = PartyAddress
                            .loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
                    for (PartyAddress contactPoint : contactPoints) {
                        if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)) {
                            phoneCount++;
                        } else if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC)
                                && contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)) {
                            emailCount++;
                        }
                    }

                }
            }
        } catch (Exception e) {
            alert.prepareError("Failed due to ", e).log();
        }

        if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATE) && customerDTO
                .getDetailToBeUpdated().contentEquals(PartyProfileDetailsConstants.PARAM_DETAILTOBEUPDATED_EMAIL)) {
            if (emailCount >= 3) {
                return false;
            }
        }
        if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATE) && customerDTO
                .getDetailToBeUpdated().contentEquals(PartyProfileDetailsConstants.PARAM_DETAILTOBEUPDATED_PHONE)) {
            if (phoneCount >= 3) {
                return false;
            }
        }
        if (customerDTO.getOperation().contentEquals(PartyProfileDetailsConstants.PARAM_OPERATION_CREATEADDRESS)) {
            if (addressCount >= 3) {
                return false;
            }
        }
        return true;

    }
    public static boolean getIntegratedFlag() 
    {
    	String integrationName = IntegrationMappings.getInstance().getIntegrationName();
    	if ("party".equalsIgnoreCase(integrationName)) {
            	return true;
            } else {
            	return false;
            }
    	
    }
 
}
