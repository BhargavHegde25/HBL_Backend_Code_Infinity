package com.kony.adminconsole.service.productmanagement.businessdelegate.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringJoiner;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.productmanagement.backenddelegate.api.FacilityBackendDelegate;
import com.kony.adminconsole.service.productmanagement.backenddelegate.api.ProductBackendDelegate;
import com.kony.adminconsole.service.productmanagement.businessdelegate.api.ProductBusinessDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class ProductBusinessDelegateImpl implements ProductBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
			
	private static final String OUTPUT_DBP_ERR_CODE = "dbpErrCode";
	private static final String OUTPUT_DBP_ERR_MESSAGE = "dbpErrMsg";
	private static final String OUTPUT_OPSTATUS = "opstatus";
	private static final String OUTPUT_PRODUCT_ID = "productId";
	private static final String INPUT_FEATURES = "features";
	private static final String OUTPUT_FEATURE_ERR_MESSAGE = "featureErrMsg";
	private static final String OUTPUT_FEATURE_ERR_CODE = "featureErrCode";
	private static final String OUTPUT_FEATURES_ENTITY_ID = "featuresEntityId";
	private static final String OUTPUT_PRODUCT_FACILITY_ID = "productFacilityId";
	private static final String INPUT_PRODUCT_FACILITIES = "productFacilities";
	private static final String OUTPUT_FACILITY_ERR_MESSAGE = "fcilityErrMsg";
	private static final String OUTPUT_FACILITY_ERR_CODE = "fcilityErrCode";
	private static final String PARAM_IMAGE_DETAILS = "imageDetails";
	private static final String OUTPUT_IMAGE_ERR_MESSAGE = "imageErrMsg";
	private static final String OUTPUT_IMAGE_ERR_CODE = "imageErrCode";
	private static final String OUTPUT_MARKETING_CATALOGUE = "marketingCatalogue";
	private static final String INPUT_PRODUCT_REF = "productRef";
	private static final String OUTPUT_FEATURE_DETAILS = "featureDetails";
	private static final String PARAM_FEATURE_ID = "featureId";
	private static final String PARAM_FEATURE_ENTITY_ID = "featureEntityId";
	private static final String PARAM_DELETE_FEATURES = "deleteFeatures";
	private static final String PARAM_UPDATE_FEATURES = "updateFeatures";
	private static final String PARAM_CREATE_FEATURES = "createFeatures";	
	private static final String PARAM_FEATURE_CREATE_ERR_MSG = "featureCreateErrMsg";
	private static final String PARAM_FEATURE_UPDATE_ERR_MSG = "featureUpdateErrMsg";
	private static final String PARAM_FEATURE_DELETE_ERR_MSG = "featureDeleteErrMsg";
	private static final String INPUT_FEATURES_ENTITY_IDS = "featuresEntityIds";
	private static final String PARAM_IMAGE_TYPE = "imageType";
	private static final String PARAM_IMAGE_CREATE_ERR_MSG = "imageCreateErrMsg";
	private static final String PARAM_IMAGE_DELETE_ERR_MSG = "imageDeleteErrMsg";
	private static final String PARAM_IMAGE_UPDATE_ERR_MSG = "imageUpdateErrMsg";
	
	private static final String PARAM_DELETE_IMAGES = "deleteImages";
	private static final String PARAM_UPDATE_IMAGES = "updateImages";
	private static final String PARAM_CREATE_IMAGES = "createImages";
	
	private static final String PARAM_FACILITIES = "facilities";
	
	
	ProductBackendDelegate productBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(ProductBackendDelegate.class);
	
	FacilityBackendDelegate facilityBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(FacilityBackendDelegate.class);
	
	@Override
	public JSONObject createProduct(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		JSONObject responseObject = null;
		try {
			
			responseObject = productBackendDelegate.createProduct(postParametersMap, backendToken);
			
			if(null != responseObject && responseObject.has(OUTPUT_PRODUCT_ID) 
					&& StringUtils.isNotBlank(responseObject.getString(OUTPUT_PRODUCT_ID))) {
				
				responseObject.remove(OUTPUT_DBP_ERR_CODE);
				responseObject.remove(OUTPUT_DBP_ERR_MESSAGE);
				
				String productId = responseObject.getString(OUTPUT_PRODUCT_ID);
				postParametersMap.put(OUTPUT_PRODUCT_ID, productId);
				
				
				//Create Product Facilities
				if(StringUtils.isNotBlank(postParametersMap.get(INPUT_PRODUCT_FACILITIES).toString()) 
						&& null != (new JSONArray(postParametersMap.get(INPUT_PRODUCT_FACILITIES).toString()))
						&& (new JSONArray(postParametersMap.get(INPUT_PRODUCT_FACILITIES).toString())).length() > 0) {
				
					try {
						
						JSONObject featuresResponseObject = productBackendDelegate.createProductFacilities(postParametersMap, backendToken);
						
						if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_PRODUCT_FACILITY_ID) 
								&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_PRODUCT_FACILITY_ID))) {
							responseObject.put(OUTPUT_PRODUCT_FACILITY_ID, featuresResponseObject.getString(OUTPUT_PRODUCT_FACILITY_ID));
							
						}else if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_DBP_ERR_CODE) 
								&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE))){
							responseObject.put(OUTPUT_FACILITY_ERR_MESSAGE, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
							responseObject.put(OUTPUT_FACILITY_ERR_CODE, featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE));
						}
						
					} catch(Exception exp) {
						alert.prepareError("Encountered exception while trying to create product facilities in MS: "+exp).log();
						responseObject.put(OUTPUT_FACILITY_ERR_MESSAGE, "Failed to create facilities");
					}
				}
				
				//Create Product Features
				if(StringUtils.isNotBlank(postParametersMap.get(INPUT_FEATURES).toString()) 
						&& null != (new JSONArray(postParametersMap.get(INPUT_FEATURES).toString()))
						&& (new JSONArray(postParametersMap.get(INPUT_FEATURES).toString())).length() > 0) {
				
					try {
						
						JSONObject featuresResponseObject = productBackendDelegate.createProductFeatures(postParametersMap, backendToken);
						
						if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_FEATURES_ENTITY_ID) 
								&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_FEATURES_ENTITY_ID))) {
							responseObject.put(OUTPUT_FEATURES_ENTITY_ID, featuresResponseObject.getString(OUTPUT_FEATURES_ENTITY_ID));
							
						}else if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_DBP_ERR_CODE) 
								&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE))){
							responseObject.put(OUTPUT_FEATURE_ERR_MESSAGE, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
							responseObject.put(OUTPUT_FEATURE_ERR_CODE, featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE));
						}
						
					} catch(Exception exp) {
						alert.prepareError("Encountered exception while trying to create product features in MS: "+exp).log();
						responseObject.put(OUTPUT_FEATURE_ERR_MESSAGE, "Failed to create features");
					}
				}
				
				//Create Product Images
				if(StringUtils.isNotBlank(postParametersMap.get(PARAM_IMAGE_DETAILS).toString()) 
						&& null != (new JSONArray(postParametersMap.get(PARAM_IMAGE_DETAILS).toString()))
						&& (new JSONArray(postParametersMap.get(PARAM_IMAGE_DETAILS).toString())).length() > 0) {
				
					try {
						
						JSONObject featuresResponseObject = productBackendDelegate.createProductImages(postParametersMap, backendToken);
						
						if(StringUtils.isBlank(featuresResponseObject.getString(OUTPUT_PRODUCT_ID))) {
							responseObject.put(OUTPUT_IMAGE_ERR_MESSAGE, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
							responseObject.put(OUTPUT_IMAGE_ERR_CODE, featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE));
							
						}
						
					} catch(Exception exp) {
						alert.prepareError("Encountered exception while trying to create product images in MS: "+exp).log();
						responseObject.put(OUTPUT_IMAGE_ERR_MESSAGE, "Failed to create images");
					}
				}
				
						
			}else if(null != responseObject) {
				responseObject.remove(OUTPUT_PRODUCT_ID);
				responseObject.remove(OUTPUT_OPSTATUS);
				responseObject.put(OUTPUT_OPSTATUS,0);
			}
			
			
		}catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to create product in MS: "+exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22135");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while Creating product in MS");
		}
			
		return responseObject;
	}

	@Override
	public JSONObject updateProduct(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		JSONObject responseObject = null;
		try {
			
			//Updating product details
			responseObject = productBackendDelegate.updateProduct(postParametersMap, backendToken);
			
			if(null != responseObject && responseObject.has(OUTPUT_PRODUCT_ID) 
					&& StringUtils.isNotBlank(responseObject.getString(OUTPUT_PRODUCT_ID))) {
				
				responseObject.remove(OUTPUT_DBP_ERR_CODE);
				responseObject.remove(OUTPUT_DBP_ERR_MESSAGE);
				if(!postParametersMap.get("marketingCatalogBackend").equals("MS")) {
				responseObject.put(OUTPUT_OPSTATUS, 0);
				}
				String productId = responseObject.getString(OUTPUT_PRODUCT_ID);
				postParametersMap.put(OUTPUT_PRODUCT_ID, productId);
				
				//Updating product features
				try {
					
					JSONObject productFeatureResponse = productBackendDelegate.getProductFeatures(postParametersMap, backendToken);

					Map<String, String> fatureEntityMap = getFatureEntityMap(productFeatureResponse);
					
					String features = postParametersMap.get(INPUT_FEATURES).toString();
					JSONObject featuresArrayJson = prepareFeaturesArray(fatureEntityMap, 
							new JSONArray(features));
					
					if(featuresArrayJson.getJSONArray(PARAM_CREATE_FEATURES).length()>0) {
						try {
							postParametersMap.put(INPUT_FEATURES, featuresArrayJson.getJSONArray(PARAM_CREATE_FEATURES));
							JSONObject featuresResponseObject = productBackendDelegate.createProductFeatures(postParametersMap, backendToken);
							if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_FEATURES_ENTITY_ID) 
									&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_FEATURES_ENTITY_ID))) {
								responseObject.put(OUTPUT_FEATURES_ENTITY_ID, featuresResponseObject.getString(OUTPUT_FEATURES_ENTITY_ID));
								
							}else if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_DBP_ERR_CODE) 
									&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE))){
								responseObject.put(PARAM_FEATURE_CREATE_ERR_MSG, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
							}
							
						} catch(Exception exp) {
							
							alert.prepareError("Encountered exception while trying to create features in MS in editFacilityFlow: "+exp).log();
							responseObject.put(PARAM_FEATURE_CREATE_ERR_MSG, "Failed to create features");
						}
					}
					
					if(featuresArrayJson.getJSONArray(PARAM_UPDATE_FEATURES).length()>0) {
						try {
							postParametersMap.put(INPUT_FEATURES, featuresArrayJson.getJSONArray(PARAM_UPDATE_FEATURES));
							JSONObject featuresResponseObject = productBackendDelegate.editProductFeatures(postParametersMap, backendToken);
							if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_DBP_ERR_CODE) 
									&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE))){
								responseObject.put(PARAM_FEATURE_UPDATE_ERR_MSG, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
							}
							
						} catch(Exception exp) {
							
							alert.prepareError("Encountered exception while trying to update features in MS in editFacilityFlow: "+exp).log();
							responseObject.put(PARAM_FEATURE_UPDATE_ERR_MSG, "Failed to update features");
						}
					}
					
					if(featuresArrayJson.getJSONArray(PARAM_DELETE_FEATURES).length()>0) {
						try {
							
							StringJoiner joiner = new StringJoiner("&");
							for(int i=0; i<featuresArrayJson.getJSONArray(PARAM_DELETE_FEATURES).length(); i++) {
								String featureEntityId = featuresArrayJson.getJSONArray(PARAM_DELETE_FEATURES).get(i).toString();
								joiner.add(PARAM_FEATURE_ID+"="+featureEntityId);
							}
							postParametersMap.put(INPUT_FEATURES_ENTITY_IDS, joiner.toString());
							postParametersMap.put(PARAM_DELETE_FEATURES, featuresArrayJson.getJSONArray(PARAM_DELETE_FEATURES));
							
							JSONObject featuresResponseObject = productBackendDelegate.deleteProductFeatures(postParametersMap, backendToken);
							if(null != featuresResponseObject && featuresResponseObject.has(OUTPUT_DBP_ERR_CODE) 
									&& StringUtils.isNotBlank(featuresResponseObject.getString(OUTPUT_DBP_ERR_CODE))){
								responseObject.put(PARAM_FEATURE_DELETE_ERR_MSG, featuresResponseObject.getString(OUTPUT_DBP_ERR_MESSAGE));
							}
							
						} catch(Exception exp) {
							
							alert.prepareError("Encountered exception while trying to delete features in MS in editFacilityFlow: "+exp).log();
							responseObject.put(PARAM_FEATURE_DELETE_ERR_MSG, "Failed to delete features");
						}
					}
					
				} catch(Exception exp) {
					alert.prepareError("Encountered exception while trying to update features for product in MS: "+exp).log();
					responseObject.put(OUTPUT_FEATURE_ERR_MESSAGE, "Failed to update feaures");
				}
				
				//Updating product Images
				try {
					if(null == postParametersMap.get(PARAM_IMAGE_DETAILS)) {
						return responseObject;
					}
					JSONObject producImageResponse = productBackendDelegate.getProductImages(postParametersMap, backendToken);
					Set<String> imageTypeSet = prepareImageTypeSet(producImageResponse);
					JSONArray inputImageDetails = new JSONArray(postParametersMap.get(PARAM_IMAGE_DETAILS).toString());
					
					if(inputImageDetails.length()>0 || imageTypeSet.size() > 0) {
						
						if(imageTypeSet.size() == 0) {
							JSONObject producImageCreateResponse = productBackendDelegate.createProductImages(postParametersMap, backendToken);
							
							if(null != producImageCreateResponse && producImageCreateResponse.has(OUTPUT_DBP_ERR_CODE) 
									&& StringUtils.isNotBlank(producImageCreateResponse.getString(OUTPUT_DBP_ERR_CODE))){
								responseObject.put(PARAM_IMAGE_CREATE_ERR_MSG, producImageCreateResponse.getString(OUTPUT_DBP_ERR_MESSAGE));
							}
						}else if(inputImageDetails.length() == 0) {
							
							for(String imageType : imageTypeSet) {
								postParametersMap.put(PARAM_IMAGE_TYPE,imageType);
								JSONObject producImagedeleteResponse = productBackendDelegate.deleteProductImages(postParametersMap, backendToken);
								
								if(null != producImagedeleteResponse && producImagedeleteResponse.has(OUTPUT_DBP_ERR_CODE) 
										&& StringUtils.isNotBlank(producImagedeleteResponse.getString(OUTPUT_DBP_ERR_CODE))){
									responseObject.put(PARAM_IMAGE_DELETE_ERR_MSG, producImagedeleteResponse.getString(OUTPUT_DBP_ERR_MESSAGE));
								}
							}
						} else {
							JSONObject imagessArrayJson = prepareImagesArray( inputImageDetails, imageTypeSet);
							
							if(imagessArrayJson.getJSONArray(PARAM_CREATE_IMAGES).length()>0) {
								
								postParametersMap.put(PARAM_IMAGE_DETAILS, imagessArrayJson.getJSONArray(PARAM_CREATE_IMAGES));
								JSONObject producImageCreateResponse = productBackendDelegate.createProductImages(postParametersMap, backendToken);
								
								if(null != producImageCreateResponse && producImageCreateResponse.has(OUTPUT_DBP_ERR_CODE) 
										&& StringUtils.isNotBlank(producImageCreateResponse.getString(OUTPUT_DBP_ERR_CODE))){
									responseObject.put(PARAM_IMAGE_CREATE_ERR_MSG, producImageCreateResponse.getString(OUTPUT_DBP_ERR_MESSAGE));
								}
							}
							
							if(imagessArrayJson.getJSONArray(PARAM_UPDATE_IMAGES).length()>0) {
								postParametersMap.put(PARAM_IMAGE_DETAILS, imagessArrayJson.getJSONArray(PARAM_UPDATE_IMAGES));
								JSONObject producImageUpdateResponse = productBackendDelegate.updateProductImages(postParametersMap, backendToken);
								
								if(null != producImageUpdateResponse && producImageUpdateResponse.has(OUTPUT_DBP_ERR_CODE) 
										&& StringUtils.isNotBlank(producImageUpdateResponse.getString(OUTPUT_DBP_ERR_CODE))){
									responseObject.put(PARAM_IMAGE_UPDATE_ERR_MSG, producImageUpdateResponse.getString(OUTPUT_DBP_ERR_MESSAGE));
								}
							}
							if(imagessArrayJson.getJSONArray(PARAM_DELETE_IMAGES).length()>0) {
								JSONArray imageArray = imagessArrayJson.getJSONArray(PARAM_DELETE_IMAGES);
								for(int i=0; i<imageArray.length();i++) {
									String imageType = imageArray.get(i).toString();
									postParametersMap.put(PARAM_IMAGE_TYPE,imageType);
									JSONObject producImagedeleteResponse = productBackendDelegate.deleteProductImages(postParametersMap, backendToken);
									
									if(null != producImagedeleteResponse && producImagedeleteResponse.has(OUTPUT_DBP_ERR_CODE) 
											&& StringUtils.isNotBlank(producImagedeleteResponse.getString(OUTPUT_DBP_ERR_CODE))){
										responseObject.put(PARAM_IMAGE_DELETE_ERR_MSG, producImagedeleteResponse.getString(OUTPUT_DBP_ERR_MESSAGE));
									}
								}
							}
						}
						
					}
				} catch(Exception exp) {
					alert.prepareError("Encountered exception while trying to update images for product in MS: "+exp).log();
					responseObject.put(OUTPUT_FEATURE_ERR_MESSAGE, "Failed to update images for product");
				}
				
			
			}else if(null != responseObject) {
				responseObject.remove(OUTPUT_PRODUCT_ID);
				responseObject.remove(OUTPUT_OPSTATUS);
				responseObject.put(OUTPUT_OPSTATUS,0);
			}
		}catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to update product in MS: "+exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22136");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while updating product in MS");
		}
			
		return responseObject;
	}

	@Override
	public JSONObject getProducts(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		JSONObject responseObject = null;
		try {
			
			if(StringUtils.isBlank(postParametersMap.get(INPUT_PRODUCT_REF).toString())) {
				responseObject = productBackendDelegate.getProductList(postParametersMap, backendToken);
			} else {
				responseObject = productBackendDelegate.getProduct(postParametersMap, backendToken);
			}
			if(null != responseObject && !responseObject.has(OUTPUT_DBP_ERR_MESSAGE) || 
					(responseObject.has(OUTPUT_DBP_ERR_MESSAGE)&& StringUtils.isBlank(responseObject.getString(OUTPUT_DBP_ERR_MESSAGE)))) {
				
				responseObject.remove(OUTPUT_DBP_ERR_CODE);
				responseObject.remove(OUTPUT_DBP_ERR_MESSAGE);
				
				if(StringUtils.isNotBlank(postParametersMap.get(INPUT_PRODUCT_REF).toString())) {
					if(responseObject.getJSONObject(OUTPUT_MARKETING_CATALOGUE).has("productGroup")
							&& responseObject.getJSONObject(OUTPUT_MARKETING_CATALOGUE).getJSONObject("productGroup").has("product")) {
						
						JSONObject product = responseObject.getJSONObject(OUTPUT_MARKETING_CATALOGUE)
											.getJSONObject("productGroup")
											.getJSONObject("product");
						JSONArray purposes = product.has("purposes")? product.getJSONArray("purposes") : null;
						JSONArray purposeModified = new JSONArray();
						if(null != purposes && purposes.length() >0 ) {
							for(int i=0; i<purposes.length();i++) {
								String purpose = purposes.get(i).toString();
								JSONObject purposeJson = new JSONObject();
								purposeJson.put("type", purpose);
								purposeModified.put(purposeJson);
							}
						}
						responseObject.getJSONObject(OUTPUT_MARKETING_CATALOGUE)
						.getJSONObject("productGroup")
						.getJSONObject("product").put("purposes", purposeModified);
						
						JSONObject masterFeatures = productBackendDelegate.getAccountLevelFeatureDetails(new HashMap<>(),null);
						
						//Modifying facility features array for client
						JSONArray facilities = product.has(INPUT_PRODUCT_FACILITIES)? 
								product.getJSONArray(INPUT_PRODUCT_FACILITIES) : new JSONArray() ;
						JSONArray facilitiesModified = new JSONArray();
						Set<String> excludeFeatures = new HashSet<String>();
						if(null !=facilities && facilities.length()> 0) {
							
							postParametersMap = new HashMap<>();
							for(int i=0; i<facilities.length(); i++) {
								JSONObject facilityJson = facilities.getJSONObject(i);
								if(!facilityJson.has(INPUT_FEATURES)) {
									facilityJson.put(INPUT_FEATURES, new JSONArray());
								}
								if(null !=facilityJson && facilityJson.has(INPUT_FEATURES)) {
									JSONArray facilityFeatures = facilityJson.getJSONArray(INPUT_FEATURES);
									facilityFeatures = prepareFeatureArryForFacility(masterFeatures.getJSONArray(INPUT_FEATURES), 
											facilityFeatures, excludeFeatures);
									facilityJson.put(INPUT_FEATURES, facilityFeatures);
								}
								
								postParametersMap.put("facilityId", facilityJson.getString("facilityId"));
								
								try {
									JSONObject facilityResponseObject = facilityBackendDelegate.getFacility(postParametersMap, backendToken);
									if(null !=facilityResponseObject && facilityResponseObject.has(PARAM_FACILITIES)
											&& facilityResponseObject.getJSONArray(PARAM_FACILITIES).length() > 0 ) {
										JSONObject facilityRespJson = facilityResponseObject.getJSONArray(PARAM_FACILITIES).getJSONObject(0);
										if(null != facilityRespJson && facilityRespJson.has("code")) {
											facilityJson.put("code", facilityRespJson.getString("code"));
										}
									}
								} catch(Exception exp) {
									alert.prepareError("Encountered exception while trying to fetch facilityCode for getProducts from MS: "+exp).log();
								}
								facilitiesModified.put(facilityJson);
							}
							
							responseObject.getJSONObject(OUTPUT_MARKETING_CATALOGUE)
							.getJSONObject("productGroup")
							.getJSONObject("product").put(INPUT_PRODUCT_FACILITIES, facilitiesModified);
						}
						
						//Modifying product features array for client
						JSONArray features = product.has(INPUT_FEATURES)? 
								product.getJSONArray(INPUT_FEATURES) : new JSONArray() ;
						JSONArray featuresModified = prepareFeatureArry(masterFeatures.getJSONArray(INPUT_FEATURES), features, excludeFeatures);
						responseObject.getJSONObject(OUTPUT_MARKETING_CATALOGUE)
						.getJSONObject("productGroup")
						.getJSONObject("product").put(INPUT_FEATURES, featuresModified);
						
					}
				}
				
			}else if(null != responseObject) {
				responseObject.remove(OUTPUT_MARKETING_CATALOGUE);
				responseObject.remove(OUTPUT_OPSTATUS);
				responseObject.put(OUTPUT_OPSTATUS,0);
			}
			
		}catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to fetch product from MS: "+exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22137");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while trying to fetch product from MS");
		}
			
		return responseObject;
		
	}

	@Override
	public JSONObject createProductFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		JSONObject responseObject = null;
		try {
			
			responseObject = productBackendDelegate.createProductFacility(postParametersMap, backendToken);
			
			if(null != responseObject && responseObject.has(OUTPUT_PRODUCT_FACILITY_ID)
					&& StringUtils.isNotBlank(responseObject.getString(OUTPUT_PRODUCT_FACILITY_ID))) {
				
				responseObject.remove(OUTPUT_DBP_ERR_CODE);
				responseObject.remove(OUTPUT_DBP_ERR_MESSAGE);
				
			}else if(null != responseObject) {
				responseObject.remove(OUTPUT_PRODUCT_FACILITY_ID);
				responseObject.remove(OUTPUT_OPSTATUS);
				responseObject.put(OUTPUT_OPSTATUS,0);
			}
			
		}catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to create productFacility in MS: "+exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22146");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while trying to create productFacility in MS");
		}
			
		return responseObject;
		
	}

	@Override
	public JSONObject updateProductFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		JSONObject responseObject = null;
		try {
			
			responseObject = productBackendDelegate.updateProductFacility(postParametersMap, backendToken);
			
			if(null != responseObject && responseObject.has(OUTPUT_PRODUCT_FACILITY_ID)
					&& StringUtils.isNotBlank(responseObject.getString(OUTPUT_PRODUCT_FACILITY_ID))) {
				
				responseObject.remove(OUTPUT_DBP_ERR_CODE);
				responseObject.remove(OUTPUT_DBP_ERR_MESSAGE);
				
			}else if(null != responseObject) {
				responseObject.remove(OUTPUT_PRODUCT_FACILITY_ID);
				responseObject.remove(OUTPUT_OPSTATUS);
				responseObject.put(OUTPUT_OPSTATUS,0);
			}
			
		}catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to update productFacility in MS: "+exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22147");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while trying to update productFacility in MS");
		}
			
		return responseObject;
	}

	@Override
	public JSONObject deleteProductFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		JSONObject responseObject = null;
		try {
			
			responseObject = productBackendDelegate.deleteProductFacility(postParametersMap, backendToken);
			
			if(null != responseObject && responseObject.has(OUTPUT_PRODUCT_FACILITY_ID)
					&& StringUtils.isNotBlank(responseObject.getString(OUTPUT_PRODUCT_FACILITY_ID))) {
				
				responseObject.remove(OUTPUT_DBP_ERR_CODE);
				responseObject.remove(OUTPUT_DBP_ERR_MESSAGE);
				
			}else if(null != responseObject) {
				responseObject.remove(OUTPUT_PRODUCT_FACILITY_ID);
				responseObject.remove(OUTPUT_OPSTATUS);
				responseObject.put(OUTPUT_OPSTATUS,0);
			}
			
		}catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to delete productFacility from MS: "+exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22148");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while trying to delete productFacility from MS");
		}
			
		return responseObject;
	}

	private Map<String, String> getFatureEntityMap(JSONObject responseJson){
		
		Map<String, String> fatureEntityMap = new HashMap<>();
		try {
			if(null != responseJson && responseJson.has(OUTPUT_FEATURE_DETAILS) 
					&& responseJson.getJSONArray(OUTPUT_FEATURE_DETAILS).length()>0) {
				
				JSONObject jsonRecord= responseJson.getJSONArray(OUTPUT_FEATURE_DETAILS).getJSONObject(0);
				if(null != jsonRecord && jsonRecord.has(INPUT_FEATURES)) {
					JSONArray features = jsonRecord.getJSONArray(INPUT_FEATURES);
					if(null != features && features.length()>0) {
						
						for(int i=0; i<features.length(); i++) {
							
							JSONObject featureJson = features.getJSONObject(i);
							String featureId = featureJson.getString(PARAM_FEATURE_ID);
							String featureEntityId = featureJson.getString(PARAM_FEATURE_ENTITY_ID);
							if(StringUtils.isAnyBlank(featureId,featureEntityId)) {
								continue;
							}
							fatureEntityMap.put(featureId, featureEntityId);
						}
					}
				}
			}
		} catch(Exception exp) {
			alert.prepareError("Encountered exception while preparing fatureEntityMap: "+exp).log();
		}
		
		return fatureEntityMap;
	}
	
	private JSONObject prepareFeaturesArray(Map<String, String> fatureEntityMap, JSONArray features) {
		
		JSONArray deleteFeatures = new JSONArray();
		JSONArray updateFeatures = new JSONArray();
		JSONArray createFeatures = new JSONArray();
		JSONObject featuresArrayJson = new JSONObject();
		featuresArrayJson.put(PARAM_DELETE_FEATURES, deleteFeatures);
		featuresArrayJson.put(PARAM_UPDATE_FEATURES, updateFeatures);
		featuresArrayJson.put(PARAM_CREATE_FEATURES, createFeatures);
		
		if(features.length() ==0) {
			for(Entry<String,String> entry :fatureEntityMap.entrySet()){
				deleteFeatures.put(entry.getValue());
			}
			featuresArrayJson.put(PARAM_DELETE_FEATURES, deleteFeatures);
			return featuresArrayJson;
		}
		if(fatureEntityMap.isEmpty()) {
			featuresArrayJson.put(PARAM_CREATE_FEATURES, features);
			return featuresArrayJson;
		}
		
		for(int i=0; i<features.length();i++) {
			
			JSONObject featureJson = features.getJSONObject(i);
			String featureId = featureJson.getString(PARAM_FEATURE_ID);
			if(fatureEntityMap.containsKey(featureId)) {
				featureJson.put(PARAM_FEATURE_ENTITY_ID, fatureEntityMap.get(featureId));
				updateFeatures.put(featureJson);
				fatureEntityMap.remove(featureId);
				continue;
			}
			createFeatures.put(featureJson);
		}
		
		for(Entry<String,String> entry :fatureEntityMap.entrySet()){
			deleteFeatures.put(entry.getValue());
		}
		
		featuresArrayJson.put(PARAM_DELETE_FEATURES, deleteFeatures);
		featuresArrayJson.put(PARAM_UPDATE_FEATURES, updateFeatures);
		featuresArrayJson.put(PARAM_CREATE_FEATURES, createFeatures);
		
		return featuresArrayJson;
	}
	
	private Set<String> prepareImageTypeSet(JSONObject imageDetails){
		
		Set<String> typeSet = new HashSet<String>();
		
		if(null != imageDetails && imageDetails.has(PARAM_IMAGE_DETAILS)) {
			try {
				JSONArray imageArray = imageDetails.getJSONArray(PARAM_IMAGE_DETAILS);
				for(int i=0; i< imageArray.length(); i++) {
					JSONObject imageRecord = imageArray.getJSONObject(i);
					String type = imageRecord.has(PARAM_IMAGE_TYPE) ? imageRecord.getString(PARAM_IMAGE_TYPE) : null;
					if(StringUtils.isNotBlank(type)) {
						typeSet.add(type);
					}
				}
				
			}catch(Exception exp) {
				alert.prepareError("Encountered exception while parsing imageDetails array: "+exp).log();
			}
		}
		
		return typeSet;
	}
	
	private JSONObject prepareImagesArray(JSONArray inputImageDetails, Set<String> imageTypeSet) {
		
		JSONArray deleteImages = new JSONArray();
		JSONArray updateImages = new JSONArray();
		JSONArray createImages = new JSONArray();
		JSONObject imagessArrayJson = new JSONObject();
		
		for(int i=0 ; i < inputImageDetails.length() ; i++ ) {
			JSONObject imgRecord =  inputImageDetails.getJSONObject(i);
			String imageType = imgRecord.getString(PARAM_IMAGE_TYPE);
			if(imageTypeSet.contains(imageType)) {
				updateImages.put(imgRecord);
			}else {
				createImages.put(imgRecord);
			}
			imageTypeSet.remove(imageType);
		}
		
		for(String type: imageTypeSet) {
			deleteImages.put(type);
		}
		
		imagessArrayJson.put(PARAM_DELETE_IMAGES, deleteImages);
		imagessArrayJson.put(PARAM_UPDATE_IMAGES, updateImages);
		imagessArrayJson.put(PARAM_CREATE_IMAGES, createImages);
		
		return imagessArrayJson;
	}
	
	private JSONArray prepareFeatureArry(JSONArray masterData, JSONArray featureData, Set<String> excludeFeatures) {
		
		JSONArray finalArray = new JSONArray();
		
		if(featureData.length() == 0) {
			JSONObject feature = null;
			JSONArray actions = null;
			for(int i=0; i<masterData.length(); i++) {
				JSONObject featureObject = new JSONObject();
				feature = masterData.getJSONObject(i);
				featureObject.put("isSelected", "false");
				featureObject.put("featureId", feature.getString("id"));
				featureObject.put("featureName", feature.getString("name"));
				featureObject.put("featureDescription", feature.getString("description"));
				featureObject.put("featureStatus", feature.getString("status"));
				JSONArray actionArray = new JSONArray();
				actions = feature.getJSONArray("actions");
				for(int j=0 ; j<actions.length(); j++) {
					JSONObject action = actions.getJSONObject(j);
					JSONObject actionObject = new JSONObject();
					actionObject.put("isSelected", "false");
					actionObject.put("actionId", action.getString("id"));
					actionObject.put("actionDescription", action.getString("description"));
					actionObject.put("actionName", action.getString("name"));
					actionObject.put("actionStatus", action.getString("status"));
					actionArray.put(actionObject);
				}
				featureObject.put("actions", actionArray);
				finalArray.put(featureObject);
			}
		} else {
			
			Map<String, Set<String>> featureMap = new HashMap<>();
			for(int i=0; i<featureData.length(); i++) {
				Set<String> actions = new HashSet<String>();
				JSONObject featureJson = featureData.getJSONObject(i);
				JSONArray actionArr = featureJson.getJSONArray("actions");
				for(int j= 0; j<actionArr.length(); j++) {
					JSONObject action = actionArr.getJSONObject(j);
					actions.add(action.getString("actionId"));
				}
				featureMap.put(featureJson.getString("featureId"), actions);
				
			}
			
			JSONObject feature = null;
			JSONArray actions = null;
			for(int i=0; i<masterData.length(); i++) {
				JSONObject featureObject = new JSONObject();
				feature = masterData.getJSONObject(i);
				String featureId = feature.getString("id");
				boolean skipActionSelected = true; 
				if(featureMap.containsKey(featureId) && !excludeFeatures.contains(featureId)) {
					featureObject.put("isSelected", "true");
					skipActionSelected = false;
				}	else {
					featureObject.put("isSelected", "false");
				}
				featureObject.put("featureId", featureId);
				featureObject.put("featureName", feature.getString("name"));
				featureObject.put("featureDescription", feature.getString("description"));
				featureObject.put("featureStatus", feature.getString("status"));
				JSONArray actionArray = new JSONArray();
				actions = feature.getJSONArray("actions");
				Set<String> selectedActions = featureMap.get(featureId);
				for(int j=0 ; j<actions.length(); j++) {
					JSONObject action = actions.getJSONObject(j);
					JSONObject actionObject = new JSONObject();
					if(skipActionSelected || !selectedActions.contains(action.getString("id"))) {
						actionObject.put("isSelected", "false");
					}else {
						actionObject.put("isSelected", "true");
					}
					
					if(action.has("dependentActions")) {
						actionObject.put("dependentActions", action.get("dependentActions"));
					}else {
						actionObject.put("dependentActions", new JSONArray());
					}
					
					actionObject.put("actionId", action.getString("id"));
					actionObject.put("actionDescription", action.getString("description"));
					actionObject.put("actionName", action.getString("name"));
					actionObject.put("actionStatus", action.getString("status"));
					actionArray.put(actionObject);
				}
				featureObject.put("actions", actionArray);
				finalArray.put(featureObject);
			}
			
		}
		
		return finalArray;
	}
	
	private JSONArray prepareFeatureArryForFacility(JSONArray masterData, JSONArray featureData, Set<String> excludeFeatures) {
		
		JSONArray finalArray = new JSONArray();
		
		if(featureData.length() == 0) {
			JSONObject feature = null;
			JSONArray actions = null;
			for(int i=0; i<masterData.length(); i++) {
				JSONObject featureObject = new JSONObject();
				feature = masterData.getJSONObject(i);
				featureObject.put("isSelected", "false");
				featureObject.put("featureId", feature.getString("id"));
				featureObject.put("featureName", feature.getString("name"));
				featureObject.put("featureDescription", feature.getString("description"));
				featureObject.put("featureStatus", feature.getString("status"));
				JSONArray actionArray = new JSONArray();
				actions = feature.getJSONArray("actions");
				for(int j=0 ; j<actions.length(); j++) {
					JSONObject action = actions.getJSONObject(j);
					JSONObject actionObject = new JSONObject();
					actionObject.put("isSelected", "false");
					actionObject.put("actionId", action.getString("id"));
					actionObject.put("actionDescription", action.getString("description"));
					actionObject.put("actionName", action.getString("name"));
					actionObject.put("actionStatus", action.getString("status"));
					actionArray.put(actionObject);
				}
				featureObject.put("actions", actionArray);
				finalArray.put(featureObject);
			}
		} else {
			
			Map<String, Set<String>> featureMap = new HashMap<>();
			for(int i=0; i<featureData.length(); i++) {
				Set<String> actions = new HashSet<String>();
				JSONObject featureJson = featureData.getJSONObject(i);
				JSONArray actionArr = featureJson.getJSONArray("actions");
				for(int j= 0; j<actionArr.length(); j++) {
					JSONObject action = actionArr.getJSONObject(j);
					actions.add(action.getString("actionsId"));
					
				}
				
				featureMap.put(featureJson.getString("featureCode"), actions);
				excludeFeatures.add(featureJson.getString("featureCode"));
				
			}
			
			JSONObject feature = null;
			JSONArray actions = null;
			for(int i=0; i<masterData.length(); i++) {
				JSONObject featureObject = new JSONObject();
				feature = masterData.getJSONObject(i);
				String featureId = feature.getString("id");
				boolean skipActionSelected = true; 
				if(featureMap.containsKey(featureId)) {
					featureObject.put("isSelected", "true");
					skipActionSelected = false;
				}	else {
					featureObject.put("isSelected", "false");
				}
				featureObject.put("featureId", featureId);
				featureObject.put("featureName", feature.getString("name"));
				featureObject.put("featureDescription", feature.getString("description"));
				featureObject.put("featureStatus", feature.getString("status"));
				JSONArray actionArray = new JSONArray();
				actions = feature.getJSONArray("actions");
				Set<String> selectedActions = featureMap.get(featureId);
				for(int j=0 ; j<actions.length(); j++) {
					JSONObject action = actions.getJSONObject(j);
					JSONObject actionObject = new JSONObject();
					if(skipActionSelected || !selectedActions.contains(action.getString("id"))) {
						actionObject.put("isSelected", "false");
					}else {
						actionObject.put("isSelected", "true");
					}
					actionObject.put("actionId", action.getString("id"));
					actionObject.put("actionDescription", action.getString("description"));
					actionObject.put("actionName", action.getString("name"));
					actionObject.put("actionStatus", action.getString("status"));
					actionArray.put(actionObject);
				}
				featureObject.put("actions", actionArray);
				finalArray.put(featureObject);
			}
			
		}
		
		return finalArray;
	}



	public String loadProductLines() throws DBPApplicationException
	{
		String response = null;

		JSONObject responseObject = productBackendDelegate.getProductLines();
		if(responseObject.has("ProductLines"))
		{
			JSONArray ProductLines=responseObject.getJSONArray("ProductLines");
			 response = productBackendDelegate.createProductLines(ProductLines);
			
		}
		
		return response;
		
	}
	
	public String loadProducts() throws DBPApplicationException
	{
		
		JSONObject responseObject = productBackendDelegate.getProducts();
		String response =null;
		
		if(responseObject.has("Products"))
		{
			JSONArray Products=responseObject.getJSONArray("Products");


			response	= productBackendDelegate.createProducts(Products);			
		
		}	

		return response;
		
	}

	@Override
	public String loadProductGroups() throws DBPApplicationException {
		// TODO Auto-generated method stub
JSONObject responseObject = productBackendDelegate.getProductGroups();
String response = null;
if(responseObject.has("ProductGroups"))
{
	JSONArray ProductGroups=responseObject.getJSONArray("ProductGroups");
	 response = productBackendDelegate.createProductGroups(ProductGroups);
	
}

		
return response;
		
	}

	@Override
	public String clearMarketingData() throws DBPApplicationException {
		
		String response = productBackendDelegate.clearMarketingData();
			
		

				
		return response;
	}

	@Override
	public String loadProductFacilities() throws DBPApplicationException {
		JSONObject responseObject = productBackendDelegate.getProductConditions();
		String response = null;
		if(responseObject.has("productFacilities"))
		{
			JSONArray ProductFacilities=responseObject.getJSONArray("productFacilities");
			 response = productBackendDelegate.createProductfacilities(ProductFacilities);
			
		}
		return response;
	}

	@Override
	public JSONObject getAllProductGroupsCampaign(Map<String, Object> postParametersMap, Map<String, Object> headerMap)
			throws DBPApplicationException {
		JSONObject responseObject = new JSONObject();
		try {

			responseObject = productBackendDelegate.getAllProductGroupsCampaign(postParametersMap, headerMap);
			
			JSONArray productsGroupsArray = new JSONArray();
			if (responseObject.has("records")) {
				JSONArray responseArray = responseObject.getJSONArray("records");
				for (int index = 0; index < responseArray.length(); index++) {
					JSONObject productGroupElem = (JSONObject) responseArray.get(index);
					String productGroupId = productGroupElem.get("productGroupId").toString();
					String productGroupName = productGroupElem.get("productGroupName").toString();
					productGroupElem.clear();
					productGroupElem.put("productGroupId", productGroupId);
					productGroupElem.put("productGroupName", productGroupName);
					productsGroupsArray.put(productGroupElem);

				}
				responseObject.remove("records");
				responseObject.put("productGroups", productsGroupsArray);
			}
			return responseObject;

		} catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to fetch product from MS: " + exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22247");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while trying to fetch product groups");
		}

		return responseObject;
	}

	@Override
	public JSONObject getProductsByProductGroup(Map<String, Object> postParametersMap, Map<String, Object> headerMap)
			throws DBPApplicationException {
		JSONObject responseObject = new JSONObject();
		try {

			responseObject = productBackendDelegate.getProductsByProductGroup(postParametersMap, headerMap);
			
			JSONArray productsArray = new JSONArray();
			JSONObject productgroupElem = new JSONObject();
			if (responseObject.has("records")) {
				JSONArray responseArray = responseObject.getJSONArray("records");
				String productGroupId = null;
				String productGroupDisplayName = null;
				
				for (int index = 0; index < responseArray.length(); index++) {
					JSONObject productElem = (JSONObject) responseArray.get(index);
					if(StringUtils.isBlank(productGroupId)) {
					productGroupId = productElem.get("productGroupId").toString();
					productGroupDisplayName = productElem.get("productGroupName").toString();
					
					}
					String productId = productElem.get("productId").toString();
					String productName = productElem.get("productName").toString();
					productElem.clear();
					productElem.put("productId", productId);
					productElem.put("productName", productName);
					productsArray.put(productElem);
				}
				
				productgroupElem.put("products", productsArray);
				productgroupElem.put("productGroupId", productGroupId);
				productgroupElem.put("productGroupDisplayName", productGroupDisplayName);
				responseObject.remove("records");
				responseObject.put("items", productgroupElem);
				
			}
			
			return responseObject;

		} catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to fetch product from MS: " + exp).log();
			responseObject = new JSONObject();
			responseObject.append(OUTPUT_DBP_ERR_CODE, "22248");
			responseObject.append(OUTPUT_DBP_ERR_MESSAGE, "Error while trying to fetch getProducts by ProductGroup");
		}

		return responseObject;
	}

}
