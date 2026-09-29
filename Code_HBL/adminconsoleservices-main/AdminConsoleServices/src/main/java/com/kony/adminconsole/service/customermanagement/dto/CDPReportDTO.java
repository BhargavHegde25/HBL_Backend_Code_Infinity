package com.kony.adminconsole.service.customermanagement.dto;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

@XmlRootElement(name="report")
@XmlAccessorType (XmlAccessType.FIELD)
public class CDPReportDTO {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	
	public CDPReportDTO() {
		super();
	}
	
	public CDPReportDTO(List<CDPReport> CDPReportDataDto) {
		super();
		this.CDPReportDataDto = CDPReportDataDto;
	}
	
	@XmlElement(name="gdpr-report-table")
	private List<CDPReport> CDPReportDataDto;
	
	public List<CDPReport> getCDPReportDataDto() {
		return CDPReportDataDto;
	}

	public void setCDPReportDataDto(List<CDPReport> cDPReportDataDto) {
		CDPReportDataDto = cDPReportDataDto;
	}

	public static Alert getLogger() {
		return alert;
	}

	public String marshallFacilitiesData(List<CDPReport> CDPReportDataDtoList) {
		String output = "";
		try {
		  CDPReportDTO CDPReportDTO = new CDPReportDTO(CDPReportDataDtoList);
		  JAXBContext context = JAXBContext.newInstance(CDPReportDTO.class);
		  Marshaller marshaller = context.createMarshaller();
		  marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
		  marshaller.setProperty(Marshaller.JAXB_FRAGMENT,Boolean.TRUE);		
		  
		  StringWriter sw = new StringWriter();
		  marshaller.marshal(CDPReportDTO, sw);
	      output = sw.toString();

		}catch(JAXBException ex) {
			alert.prepareError("Error in FacilitiesDTO : marshallFacilitiesData"+ ex.getMessage()).log();
		}
		  return output;
	}
}
