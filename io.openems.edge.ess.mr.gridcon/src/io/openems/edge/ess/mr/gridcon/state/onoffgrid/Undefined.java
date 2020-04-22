package io.openems.edge.ess.mr.gridcon.state.onoffgrid;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.ess.mr.gridcon.IState;
import io.openems.edge.ess.mr.gridcon.StateObject;

public class Undefined extends BaseState implements StateObject {

	private final Logger log = LoggerFactory.getLogger(Undefined.class);

	public Undefined(ComponentManager manager, DecisionTableCondition condition, String gridconPCSId, String b1Id, String b2Id, String b3Id,
			String inputNA1, String inputNA2, String inputSyncBridge, String outputSyncBridge, String meterId, boolean na1Inverted, boolean na2Inverted, boolean inputSyncInverted) {
		super(manager, condition, gridconPCSId, b1Id, b2Id, b3Id, inputNA1, inputNA2, inputSyncBridge, outputSyncBridge, meterId, na1Inverted, na2Inverted, inputSyncInverted);
	}

	@Override
	public IState getState() {
		return io.openems.edge.ess.mr.gridcon.state.onoffgrid.OnOffGridState.UNDEFINED;
	}

	@Override
	public IState getNextState() {
		if (DecisionTableHelper.isUndefined(condition)) {
			return OnOffGridState.UNDEFINED;
		}
		
		if (DecisionTableHelper.isStateStartSystem(condition)) {
			if (getStateBefore() == null || getStateBefore() == OnOffGridState.START_SYSTEM) {
				return OnOffGridState.START_SYSTEM;
			}
		}
		
		if (DecisionTableHelper.isWaitingForDevices(condition)) {
			if (getStateBefore() == null || Arrays.asList(OnOffGridState.START_SYSTEM, OnOffGridState.WAIT_FOR_DEVICES).contains(getStateBefore())) {
				return OnOffGridState.WAIT_FOR_DEVICES;
			}
		}
		
		if (DecisionTableHelper.isOnGridMode(condition)) {	
			System.out.println("DecisionTableHelper -->  On grid conditions!");
			if (getStateBefore() == null || Arrays.asList(OnOffGridState.START_SYSTEM, OnOffGridState.WAIT_FOR_DEVICES, OnOffGridState.ON_GRID_MODE).contains(getStateBefore())) {
				return OnOffGridState.ON_GRID_MODE;
			} else {
				System.out.println("state before not ok!!!");
			}
		}
		
		if (DecisionTableHelper.isOffGridMode(condition)) {			
			if (getStateBefore() == null || Arrays.asList(OnOffGridState.ON_GRID_MODE, OnOffGridState.OFF_GRID_MODE, OnOffGridState.OFF_GRID_MODE_GRID_BACK, OnOffGridState.OFF_GRID_MODE_WAIT_FOR_GRID_AVAILABLE, OnOffGridState.OFF_GRID_MODE_ADJUST_PARMETER).contains(getStateBefore())) {
				return OnOffGridState.OFF_GRID_MODE;
			}
		}
		
		if (DecisionTableHelper.isOffGridGridBack(condition)) {			
			if (getStateBefore() == null || Arrays.asList(OnOffGridState.OFF_GRID_MODE, OnOffGridState.OFF_GRID_MODE_GRID_BACK).contains(getStateBefore())) {
				return OnOffGridState.OFF_GRID_MODE_GRID_BACK;
			}
		}
		
		if (DecisionTableHelper.isOffGridWaitForGridAvailable(condition)) {			
			if (getStateBefore() == null || Arrays.asList(OnOffGridState.OFF_GRID_MODE_WAIT_FOR_GRID_AVAILABLE, OnOffGridState.OFF_GRID_MODE_GRID_BACK).contains(getStateBefore())) {
				return OnOffGridState.OFF_GRID_MODE_WAIT_FOR_GRID_AVAILABLE;
			}
		}
		
		if (DecisionTableHelper.isAdjustParameters(condition)) {			
			if (getStateBefore() == null || Arrays.asList(OnOffGridState.OFF_GRID_MODE_WAIT_FOR_GRID_AVAILABLE, OnOffGridState.OFF_GRID_MODE_ADJUST_PARMETER).contains(getStateBefore())) {
				return OnOffGridState.OFF_GRID_MODE_ADJUST_PARMETER;
			}
		}
		
//		if (DecisionTableHelper.isRestartGridconAfterSync(condition)) {			
//			if (getStateBefore() == null || Arrays.asList(OnOffGridState.ON_GRID_RESTART_GRIDCON_AFTER_SYNC).contains(getStateBefore())) {
//				return OnOffGridState.ON_GRID_RESTART_GRIDCON_AFTER_SYNC;
//			}
//		}
		
		return OnOffGridState.UNDEFINED;
	}

	@Override
	public void act() {
		log.info("Nothing to do!");
	}
}
