package ricordo.owlkb.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ricordo.owlkb.rest.bean.Term;
import ricordo.owlkb.rest.bean.TermList;
import ricordo.owlkb.rest.service.OwlKbService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sarala Wimalaratne
 * Date: 05/03/12
 * Time: 13:07
 */

@Controller
public class TermController {
    private OwlKbService owlKbService;

    private static final String XML_VIEW_NAME = "terms";

    @RequestMapping(method=RequestMethod.GET, value="/terms/{query}")
    public ModelAndView getTerms(@PathVariable String query) {
        List<Term> employees = owlKbService.getTerms(query);
        TermList list = new TermList(employees);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }


	@RequestMapping(method=RequestMethod.GET, value="/termsTest/{query}")
    public ModelAndView getTermsTest(@PathVariable String query) {
        List<Term> employees = owlKbService.getTermsNoReasoner(query);
        TermList list = new TermList(employees);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.GET, value="/subterms/{query}")
    public ModelAndView getSubTerms(@PathVariable String query) {
        List<Term> employees = owlKbService.getSubTerms(query);
        TermList list = new TermList(employees);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.GET, value="/eqterms/{query}")
    public ModelAndView getEqTerms(@PathVariable String query) {
        List<Term> employees = owlKbService.getEquivalentTerms(query);
        TermList list = new TermList(employees);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.POST, value="/addterm/{query}")
    public ModelAndView addTerm(@PathVariable String query) {
        List<Term> terms = owlKbService.addTerm(query);
        TermList list = new TermList(terms);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/deleteterm/{query}")
    public ModelAndView removeEmployee(@PathVariable String query) {
        List<Term> terms = owlKbService.deleteTerm(query);
        TermList list = new TermList(terms);
        return new ModelAndView(XML_VIEW_NAME, "terms", list);
    }

    public void setOwlKbService(OwlKbService service) {
        this.owlKbService = service;
    }
}
