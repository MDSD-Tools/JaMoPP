package meet_eat.server.controller;

import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Report;
import meet_eat.server.service.ReportService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents an concrete controller class handling incoming RESTful CRUD requests by providing specific endpoints
 * especially for {@link Report} entities.
 */
@RestController
public class ReportController extends EntityController<Report, String, ReportService> {

    /**
     * Constructs a new instance of {@link ReportController}.
     *
     * @param entityService   the {@link ReportService} used by this controller
     * @param securityService the {@link SecurityService} usedO by this controller
     */
    @Lazy
    @Autowired
    public ReportController(ReportService entityService, SecurityService<Report> securityService) {
        super(entityService, securityService);
    }

    /**
     * Posts a new {@link Report report} into the persistence layer.
     *
     * @param report the report to be posted
     * @param token  the authentication token of the requester
     * @return the posted report within a {@link ResponseEntity}
     */
    @PostMapping(EndpointPath.REPORTS)
    public ResponseEntity<Report> postReport(@RequestBody Report report,
                                             @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePost(report, token);
    }
}
