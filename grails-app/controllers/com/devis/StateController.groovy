package com.devis

class StateController {
	static scaffold = true
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [stateInstanceList: State.list(params), stateInstanceTotal: State.count()]
    }

    def create = {
        def stateInstance = new State()
        stateInstance.properties = params
        return [stateInstance: stateInstance]
    }

    def save = {
        def stateInstance = new State(params)
        if (stateInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'state.label', default: 'State'), stateInstance.id])}"
            redirect(action: "show", id: stateInstance.id)
        }
        else {
            render(view: "create", model: [stateInstance: stateInstance])
        }
    }

    def show = {
        def stateInstance = State.get(params.id)
        if (!stateInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'state.label', default: 'State'), params.id])}"
            redirect(action: "list")
        }
        else {
            [stateInstance: stateInstance]
        }
    }

    def edit = {
        def stateInstance = State.get(params.id)
        if (!stateInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'state.label', default: 'State'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [stateInstance: stateInstance]
        }
    }

    def update = {
        def stateInstance = State.get(params.id)
        if (stateInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (stateInstance.version > version) {
                    
                    stateInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'state.label', default: 'State')] as Object[], "Another user has updated this State while you were editing")
                    render(view: "edit", model: [stateInstance: stateInstance])
                    return
                }
            }
            stateInstance.properties = params
            if (!stateInstance.hasErrors() && stateInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'state.label', default: 'State'), stateInstance.id])}"
                redirect(action: "show", id: stateInstance.id)
            }
            else {
                render(view: "edit", model: [stateInstance: stateInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'state.label', default: 'State'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def stateInstance = State.get(params.id)
        if (stateInstance) {
            try {
                stateInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'state.label', default: 'State'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'state.label', default: 'State'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'state.label', default: 'State'), params.id])}"
            redirect(action: "list")
        }
    }
}
