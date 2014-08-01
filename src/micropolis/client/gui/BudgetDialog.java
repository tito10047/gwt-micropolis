package micropolis.client.gui;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import micropolis.client.engine.*;

public class BudgetDialog extends DialogBox {
    private final TextBox taxRateHdrInput;
    private final HTML annualReceiptsHdr;
    private final TextBox fondRoadFoundingLevelHdrRange;
    private final HTML fondRoadRequestedHdrLabel;
    private final HTML fondRoadAllocationHdrLabel;
    private final HTML policeRoadAllocationHdrLabel;
    private final TextBox policeRoadFoundingLevelHdrRange;
    private final HTML policeRoadRequestedHdrLabel;
    private final TextBox fireRoadFoundingLevelHdrRange;
    private final HTML fireRoadRequestedHdrLabel;
    private final HTML fireRoadAllocationHdrLabel;
    private final CheckBox autoBudgetCheck;
    private final CheckBox pauseGameCheck;
    private final Node closeEventTarget;
    HTML[][] balanceLabels;
    Micropolis engine;

	//JSpinner taxRateEntry;
	int origTaxRate;
	double origRoadPct;
	double origFirePct;
	double origPolicePct;
    private HandlerRegistration closeHandler=null;

    public BudgetDialog(Micropolis engine) {
		/*super(owner);
		setTitle(strings.getString("budgetdlg.title"));*/


        getCellElement(0, 1).getStyle().setCursor(Style.Cursor.MOVE);
        Element dialogTopRight = getCellElement(0, 2);
        dialogTopRight.setInnerHTML("<div style=\"margin-left:-15px;margin-top: 5px;cursor: pointer;\"><img src=\"/resources/closewindow.png\" /></div>");
        closeEventTarget = dialogTopRight.getChild(0).getChild(0);

        setEngine(engine);

        ChangeHandler changeHandler = new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                applyChange();
            }
        };

        FlexTable table = new FlexTable();
        FlexTable.FlexCellFormatter gridFormater = table.getFlexCellFormatter();
        table.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

        int row = 0;
        gridFormater.setColSpan(row, 0, 2);
        gridFormater.setColSpan(row, 1, 2);
        gridFormater.setColSpan(row, 2, 2);
        table.setWidget(row, 1, new HTML(MainWindow.guiStrings.get("budgetdlg.tax_rate_hdr")));
        table.setWidget(row, 2,new HTML(MainWindow.guiStrings.get("budgetdlg.annual_receipts_hdr")));

        row++;
        gridFormater.setColSpan(row, 0, 2);
        gridFormater.setColSpan(row, 1, 2);
        gridFormater.setColSpan(row, 2, 2);
        taxRateHdrInput = new TextBox(/*0,20*/);
    	taxRateHdrInput.getElement().setAttribute("type", "number");
        taxRateHdrInput.getElement().setAttribute("min", "0");
        taxRateHdrInput.getElement().setAttribute("max", "20");
        taxRateHdrInput.addChangeHandler(changeHandler);
        annualReceiptsHdr = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.tax_revenue")));
        table.setWidget(row, 1, taxRateHdrInput);
        table.setWidget(row, 2, annualReceiptsHdr);

        row++;
        gridFormater.setColSpan(row, 0, 6);
        table.setWidget(row, 0, new HTML("<hr  style=\"width:100%;\" />"));

        row++;
        gridFormater.setColSpan(row, 2, 2);
        table.setWidget(row, 1, new HTML(MainWindow.guiStrings.get("budgetdlg.funding_level_hdr")));
        table.setWidget(row, 2, new HTML(MainWindow.guiStrings.get("budgetdlg.requested_hdr")));
        table.setWidget(row, 3,new HTML(MainWindow.guiStrings.get("budgetdlg.allocation_hdr")));

        row++;
        gridFormater.setColSpan(row, 2, 2);
        fondRoadFoundingLevelHdrRange = new TextBox(/*0,100*/);
    	fondRoadFoundingLevelHdrRange.getElement().setAttribute("type", "range");
        fondRoadFoundingLevelHdrRange.getElement().setAttribute("min", "0");
        fondRoadFoundingLevelHdrRange.getElement().setAttribute("max", "100");
        
        fondRoadFoundingLevelHdrRange.setWidth(100+"px");
        fondRoadFoundingLevelHdrRange.addChangeHandler(changeHandler);
        fondRoadRequestedHdrLabel = new HTML();
        fondRoadAllocationHdrLabel = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.road_fund")));
        table.setWidget(row, 1, fondRoadFoundingLevelHdrRange);
        table.setWidget(row, 2, fondRoadRequestedHdrLabel);
        table.setWidget(row, 3, fondRoadAllocationHdrLabel);

        row++;
        gridFormater.setColSpan(row, 2, 2);
        policeRoadFoundingLevelHdrRange = new TextBox(/*0,100*/);
    	policeRoadFoundingLevelHdrRange.getElement().setAttribute("type", "range");
        policeRoadFoundingLevelHdrRange.getElement().setAttribute("min", "0");
        policeRoadFoundingLevelHdrRange.getElement().setAttribute("max", "100");
        policeRoadFoundingLevelHdrRange.setWidth(100+"px");
        policeRoadFoundingLevelHdrRange.addChangeHandler(changeHandler);
        policeRoadRequestedHdrLabel = new HTML();
        policeRoadAllocationHdrLabel = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.police_fund")));
        table.setWidget(row, 1, policeRoadFoundingLevelHdrRange);
        table.setWidget(row, 2, policeRoadRequestedHdrLabel);
        table.setWidget(row, 3, policeRoadAllocationHdrLabel);

        row++;
        gridFormater.setColSpan(row, 2, 2);
        fireRoadFoundingLevelHdrRange = new TextBox(/*0,100*/);
    	fireRoadFoundingLevelHdrRange.getElement().setAttribute("type", "range");
        fireRoadFoundingLevelHdrRange.getElement().setAttribute("min", "0");
        fireRoadFoundingLevelHdrRange.getElement().setAttribute("max", "100");
        fireRoadFoundingLevelHdrRange.setWidth(100+"px");
        fireRoadFoundingLevelHdrRange.addChangeHandler(changeHandler);
        fireRoadRequestedHdrLabel = new HTML();
        fireRoadAllocationHdrLabel = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.fire_fund")));
        table.setWidget(row, 1, fireRoadFoundingLevelHdrRange);
        table.setWidget(row, 2, fireRoadRequestedHdrLabel);
        table.setWidget(row, 3, fireRoadAllocationHdrLabel);

        row++;
        gridFormater.setColSpan(row, 0, 6);
        table.setWidget(row, 0, new HTML("<hr  style=\"width:100%;\" />"));

        row++;
        gridFormater.setColSpan(row, 0, 2);
        gridFormater.setColSpan(row, 1, 2);
        gridFormater.setColSpan(row, 2, 2);
        gridFormater.getElement(row,0).getStyle().setTextAlign(Style.TextAlign.RIGHT);
        HTML periodEndingLabel = new HTML();
        HTML periodEndingLabel2 = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.period_ending")));
        table.setWidget(row, 1, periodEndingLabel);
        table.setWidget(row, 2, periodEndingLabel2);

        row++;
        gridFormater.setColSpan(row, 0, 2);
        gridFormater.setColSpan(row, 1, 2);
        gridFormater.setColSpan(row, 2, 2);
        gridFormater.getElement(row,0).getStyle().setTextAlign(Style.TextAlign.RIGHT);
        HTML cashBeginLabel = new HTML();
        HTML cashBeginLabel2 = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.cash_begin")));
        table.setWidget(row, 1, cashBeginLabel);
        table.setWidget(row, 2, cashBeginLabel2);

        row++;
        gridFormater.setColSpan(row, 0, 2);
        gridFormater.setColSpan(row, 1, 2);
        gridFormater.setColSpan(row, 2, 2);
        gridFormater.getElement(row,0).getStyle().setTextAlign(Style.TextAlign.RIGHT);
        HTML taxesCollectedLabel = new HTML();
        HTML taxesCollectedLabel2 = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.taxes_collected")));
        table.setWidget(row, 1, taxesCollectedLabel);
        table.setWidget(row, 2, taxesCollectedLabel2);

        row++;
        gridFormater.setColSpan(row, 0, 2);
        gridFormater.setColSpan(row, 1, 2);
        gridFormater.setColSpan(row, 2, 2);
        gridFormater.getElement(row,0).getStyle().setTextAlign(Style.TextAlign.RIGHT);
        HTML capitalExpensesLabel = new HTML();
        HTML capitalExpensesLabel2 = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.capital_expenses")));
        table.setWidget(row, 1, capitalExpensesLabel);
        table.setWidget(row, 2, capitalExpensesLabel2);

        row++;
        gridFormater.setColSpan(row, 0, 2);
        gridFormater.setColSpan(row, 1, 2);
        gridFormater.setColSpan(row, 2, 2);
        gridFormater.getElement(row,0).getStyle().setTextAlign(Style.TextAlign.RIGHT);
        HTML operatingExpensesLabel = new HTML();
        HTML operatingExpensesLabel2 = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.operating_expenses")));
        table.setWidget(row, 1, operatingExpensesLabel);
        table.setWidget(row, 2, operatingExpensesLabel2);

        row++;
        gridFormater.setColSpan(row, 0, 2);
        gridFormater.setColSpan(row, 1, 2);
        gridFormater.setColSpan(row, 2, 2);
        gridFormater.getElement(row,0).getStyle().setTextAlign(Style.TextAlign.RIGHT);
        HTML cashEndLabel = new HTML();
        HTML cashEndLabel2 = new HTML();
        table.setWidget(row, 0, new HTML(MainWindow.guiStrings.get("budgetdlg.cash_end")));
        table.setWidget(row, 1, cashEndLabel);
        table.setWidget(row, 2, cashEndLabel2);

        balanceLabels = new HTML[][]{
                new HTML[]{periodEndingLabel, periodEndingLabel2},
                new HTML[]{cashBeginLabel, cashBeginLabel2},
                new HTML[]{taxesCollectedLabel, taxesCollectedLabel2},
                new HTML[]{capitalExpensesLabel, capitalExpensesLabel2},
                new HTML[]{operatingExpensesLabel, operatingExpensesLabel2},
                new HTML[]{cashEndLabel, cashEndLabel2},
        };

        row++;
        gridFormater.setColSpan(row, 0, 6);
        table.setWidget(row, 0, new HTML("<hr  style=\"width:100%;\" />"));

        row++;
        gridFormater.setColSpan(row, 0, 3);
        gridFormater.setColSpan(row, 1, 3);
        autoBudgetCheck = new CheckBox(MainWindow.guiStrings.get("budgetdlg.auto_budget"));
        pauseGameCheck = new CheckBox(MainWindow.guiStrings.get("budgetdlg.pause_game"));
        table.setWidget(row, 0, autoBudgetCheck);
        table.setWidget(row, 1, pauseGameCheck);


        row++;
        gridFormater.setColSpan(row, 0, 3);
        gridFormater.setColSpan(row, 1, 3);
        Button continueButton = new Button(MainWindow.guiStrings.get("budgetdlg.continue"));
        Button resetButton = new Button(MainWindow.guiStrings.get("budgetdlg.reset"));
        table.setWidget(row, 0, continueButton);
        table.setWidget(row, 1, resetButton);

        continueButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                onContinueClicked();
            }
        });
        resetButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onResetClicked();
            }
        });
        add(table);
    }

    public void show(CloseHandler<PopupPanel> closeHandler) {
        if (this.closeHandler != null) {
            this.closeHandler.removeHandler();
        }
        this.closeHandler = addCloseHandler(closeHandler);
        loadBudgetNumbers(true);
        loadBalance();
        show();
    }

	private void applyChange() {
		int newTaxRate = Integer.parseInt(taxRateHdrInput.getValue());
		int newRoadPct = Integer.parseInt(fondRoadFoundingLevelHdrRange.getValue());
		int newPolicePct = Integer.parseInt( policeRoadFoundingLevelHdrRange.getValue());
		int newFirePct = Integer.parseInt( fireRoadFoundingLevelHdrRange.getValue());

		engine.cityTax = checkRange(newTaxRate,0,20);
		engine.roadPercent = (double) checkRange(newRoadPct,0,100) / 100.0;
		engine.policePercent = (double) checkRange(newPolicePct,0,100) / 100.0;
		engine.firePercent = (double) checkRange(newFirePct,0,100) / 100.0;

		loadBudgetNumbers(false);
	}
    private int checkRange(int val, int min, int max){
        return Math.max(min,Math.min(max, val));
    }

	private void loadBudgetNumbers(boolean updateEntries) {
		BudgetNumbers b = engine.generateBudget();
		if (updateEntries) {
            taxRateHdrInput.setText(b.taxRate + "");
			fondRoadFoundingLevelHdrRange.setValue( Math.round(b.roadPercent * 100.0)+"");
			policeRoadFoundingLevelHdrRange.setValue( Math.round(b.policePercent * 100.0)+"");
			fireRoadFoundingLevelHdrRange.setValue( Math.round(b.firePercent * 100.0)+"");
		}
        annualReceiptsHdr.setText("$"+b.taxIncome);

        fondRoadRequestedHdrLabel.setText("$" + b.roadRequest);
        fondRoadAllocationHdrLabel.setText("$" + b.roadFunded);

        policeRoadRequestedHdrLabel.setText("$"+b.policeRequest);
        policeRoadAllocationHdrLabel.setText("$"+b.policeFunded);

        fireRoadRequestedHdrLabel.setText("$"+b.fireRequest);
        fireRoadAllocationHdrLabel.setText("$"+b.fireFunded);
	}

	private void onContinueClicked() {

		if (autoBudgetCheck.getValue() != engine.autoBudget) {
			engine.toggleAutoBudget();
		}
		if (pauseGameCheck.getValue() && engine.simSpeed != Speed.PAUSED) {
			engine.setSpeed(Speed.PAUSED);
		} else if (!pauseGameCheck.getValue() && engine.simSpeed == Speed.PAUSED) {
			engine.setSpeed(Speed.NORMAL);
		}

		hide();
	}

    private void loadBalance(){
        int x=-1;
        for (int i = 0; i < 2; i++) {

            if (i + 1 >= engine.financialHistory.size()) {
                break;
            }
            Micropolis.FinancialHistory f = engine.financialHistory.get(i);
            Micropolis.FinancialHistory fPrior = engine.financialHistory.get(i+1);
            int cashFlow = f.totalFunds - fPrior.totalFunds;
            int capExpenses = -(cashFlow - f.taxIncome + f.operatingExpenses);
            int y=0;x++;
            balanceLabels[y++][x].setText(formatGameDate(f.cityTime-1));
            balanceLabels[y++][x].setText(formatFunds(f.totalFunds));
            balanceLabels[y++][x].setText(formatFunds(f.taxIncome));
            balanceLabels[y++][x].setText(formatFunds(capExpenses));
            balanceLabels[y++][x].setText(formatFunds(f.operatingExpenses));
            balanceLabels[y][x].setText(formatFunds(f.totalFunds));
        }
    }

	private void onResetClicked() {
		engine.cityTax = this.origTaxRate;
		engine.roadPercent = this.origRoadPct;
		engine.firePercent = this.origFirePct;
		engine.policePercent = this.origPolicePct;
		loadBudgetNumbers(true);
	}


    public void setEngine(Micropolis engine) {
        this.engine = engine;
        this.origTaxRate = engine.cityTax;
        this.origRoadPct = engine.roadPercent;
        this.origFirePct = engine.firePercent;
        this.origPolicePct = engine.policePercent;
    }
    @Override
    protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        NativeEvent nativeEvent = event.getNativeEvent();

        if (!event.isCanceled()
                && (event.getTypeInt() == Event.ONCLICK)
                && isCloseEvent(nativeEvent))
        {
            this.hide();
        }
        super.onPreviewNativeEvent(event);
    }
    private boolean isCloseEvent(NativeEvent event) {
        return event.getEventTarget().equals(closeEventTarget);
    }
    public static String formatGameDate(int cityTime)
    {
        int year = 1990 + cityTime/48;
        int month = (cityTime%48)/4 + 1;
        int day =  (cityTime%4)*7 + 1;
        return day+"."+month+" "+year;
    }
    public static String formatFunds(Integer funds) {
        return MainWindow.guiStrings.format("funds", funds);
    }
}
