package gui_components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import models.GoalModel;
import models.ProfileModel;
import observables.StonksObservable;
import stonks.Constants;

public class SideMenu implements Constants, PropertyChangeListener {

    private final StonksObservable stonksObs;
    private final VBox rootDiv;

    private HBox profileDiv;
    private VBox linkDiv;
    private StackPane goalDiv;
    private VBox topGoalsDiv;
    private BorderPane labelDiv;
    private Label profileIcon;
    private Color profileColor;
    private Color textColor;
    private VBox profileInfoDiv;
    private Label profileFirstName;
    private Label profileLastName;
    private Label profileLink;
    private Label logoutLink;
    private Label dashboardLink;
    private Label goalLink;
    private PseudoClass last;
    private PseudoClass active;

    private List<MiniGoalBox> topGoals;
    private Label lblNoGoalsToShow;
    
    private final PseudoClass selected_black;
    private final PseudoClass selected_white;

    public SideMenu(StonksObservable stonksObs) {
        this.stonksObs = stonksObs;
        rootDiv = new VBox();

        rootDiv.setMinSize(SIDEMENU_WIDTH, SIDEMENU_HEIGHT);
        rootDiv.setMaxSize(SIDEMENU_WIDTH, SIDEMENU_HEIGHT);

        rootDiv.setId("sideMenu");
        
        selected_black = PseudoClass.getPseudoClass("selected_black");
        selected_white = PseudoClass.getPseudoClass("selected_white");

        setupProfileDiv();
        setupLinkDiv();
        setupGoalDiv();
        setupEventListeners();
        setupPropertyChangeListeners();
    }

    private void setupPropertyChangeListeners() {
        stonksObs.addPropertyChangeListener(STONKS_EVENT.GOTO_PROFILE_VIEW.name(), this);
        stonksObs.addPropertyChangeListener(STONKS_EVENT.GOTO_GOAL_VIEW.name(), this);
        stonksObs.addPropertyChangeListener(STONKS_EVENT.GOTO_DASHBOARD_VIEW.name(), this);
        stonksObs.addPropertyChangeListener(STONKS_EVENT.PROFILE_HAS_BEEN_AUTH.name(), this);
        stonksObs.addPropertyChangeListener(STONKS_EVENT.PROFILE_HAS_BEEN_EDITED.name(), this);
        stonksObs.addPropertyChangeListener(STONKS_EVENT.GOAL_STATE_CHANGED.name(), this);
    }

    private void setupProfileDiv() {
        profileDiv = new HBox();
        profileDiv.setMinSize(SIDEMENU_WIDTH, SIDEMENU_HEIGHT * 0.15);
        profileDiv.setMaxSize(SIDEMENU_WIDTH, SIDEMENU_HEIGHT * 0.15);
        profileDiv.setId("profile");

        profileIcon = new Label();
        profileIcon.setMinSize(70, 70);
        profileIcon.setId("profileIcon");

        profileInfoDiv = new VBox();
        profileInfoDiv.setId("profileInfo");
        profileFirstName = new Label();
        profileFirstName.setId("firstName");
        profileLastName = new Label();
        profileLastName.setId("lastName");
        logoutLink = new Label("Logout");
        logoutLink.setId("logout");
        logoutLink.getStyleClass().addAll("link");
        
        profileInfoDiv.getChildren().addAll(profileFirstName, profileLastName, logoutLink);
        
        profileDiv.getChildren().addAll(profileIcon, profileInfoDiv);

        rootDiv.getChildren().add(profileDiv);
    }

    private void setupLinkDiv() {
        linkDiv = new VBox();

        linkDiv.setMinSize(SIDEMENU_WIDTH, SIDEMENU_HEIGHT * 0.21);
        linkDiv.setMaxSize(SIDEMENU_WIDTH, SIDEMENU_HEIGHT * 0.21);
        linkDiv.setId("linkDiv");

        last = PseudoClass.getPseudoClass("last");
        active = PseudoClass.getPseudoClass("active");

        profileLink = new Label("Profile");
        profileLink.setMinWidth(SIDEMENU_WIDTH);
        profileLink.setMinHeight(SIDEMENU_HEIGHT * 0.07);

        dashboardLink = new Label("Dashboard");
        dashboardLink.setMinWidth(SIDEMENU_WIDTH);
        dashboardLink.setMinHeight(SIDEMENU_HEIGHT * 0.07);

        goalLink = new Label("Goals");
        goalLink.setMinWidth(SIDEMENU_WIDTH);
        goalLink.setMinHeight(SIDEMENU_HEIGHT * 0.07);
        goalLink.pseudoClassStateChanged(last, true);

        linkDiv.getChildren().addAll(profileLink, dashboardLink, goalLink);

        rootDiv.getChildren().add(linkDiv);
    }

    private void setupGoalDiv() {
        goalDiv = new StackPane();
        goalDiv.setMinSize(SIDEMENU_WIDTH, SIDEMENU_HEIGHT * 0.64);
        goalDiv.setMaxSize(SIDEMENU_WIDTH, SIDEMENU_HEIGHT * 0.64);
        
        topGoalsDiv = new VBox();
        labelDiv = new BorderPane();
        
        lblNoGoalsToShow = new Label("No goals in progress...");
        lblNoGoalsToShow.setId("infoLabel");
        labelDiv.setCenter(lblNoGoalsToShow);

        topGoalsDiv.setId("goalDiv");
        
        topGoals = new ArrayList<>();
        MiniGoalBox tempMGB;
        for(int i = 0; i < 5; i++){
            tempMGB = new MiniGoalBox();
            topGoalsDiv.getChildren().addAll(tempMGB.getRoot());
            topGoals.add(tempMGB);
        }
        
        goalDiv.getChildren().addAll(topGoalsDiv, labelDiv);
        rootDiv.getChildren().add(goalDiv);
    }

    public Pane getRoot() {
        return rootDiv;
    }

    private void setupEventListeners() {
        logoutLink.setOnMouseClicked(e -> {
            stonksObs.logout();
        });
        profileLink.setOnMouseClicked(e -> {
            stonksObs.firePropertyChange(STONKS_EVENT.GOTO_PROFILE_VIEW.name(), null, null);
        });
        dashboardLink.setOnMouseClicked(e -> {
            stonksObs.firePropertyChange(STONKS_EVENT.GOTO_DASHBOARD_VIEW.name(), null, null);
        });
        goalLink.setOnMouseClicked(e -> {
            stonksObs.firePropertyChange(STONKS_EVENT.GOTO_GOAL_VIEW.name(), null, null);
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(STONKS_EVENT.GOTO_PROFILE_VIEW.name())) {
            resetPseudoClasses();
            profileLink.pseudoClassStateChanged(active, true);
        } else if (evt.getPropertyName().equals(STONKS_EVENT.GOTO_GOAL_VIEW.name())) {
            resetPseudoClasses();
            goalLink.pseudoClassStateChanged(active, true);
        } else if (evt.getPropertyName().equals(STONKS_EVENT.GOTO_DASHBOARD_VIEW.name())) {
            resetPseudoClasses();
            dashboardLink.pseudoClassStateChanged(active, true);
        } else if (evt.getPropertyName().equals(STONKS_EVENT.PROFILE_HAS_BEEN_AUTH.name())) {
            updateSideMenu();
            updateMiniGoalBoxes();
        } else if (evt.getPropertyName().equals(STONKS_EVENT.PROFILE_HAS_BEEN_EDITED.name())) {
            updateSideMenu();
        } else if (evt.getPropertyName().equals(STONKS_EVENT.GOAL_STATE_CHANGED.name())) {
            updateMiniGoalBoxes();
        }
    }

    private void resetPseudoClasses(){
        profileLink.pseudoClassStateChanged(active, false);
        goalLink.pseudoClassStateChanged(active, false);
        dashboardLink.pseudoClassStateChanged(active, false);
    }
    
    private void updateSideMenu() {
        ProfileModel authProfile = stonksObs.getAuthProfile();

        profileFirstName.setText(authProfile.getFirstName());
        profileLastName.setText(authProfile.getLastName());
        
        profileIcon.setText(""
                + authProfile.getFirstName().charAt(0)
                + authProfile.getLastName().charAt(0));

        profileColor = Color.valueOf(authProfile.getColor());

        if (((profileColor.getRed() * 0.333)
                + (profileColor.getGreen() * 0.333)
                + (profileColor.getBlue() * 0.333)) > 0.3) {
            profileIcon.pseudoClassStateChanged(selected_black, true);
            profileIcon.pseudoClassStateChanged(selected_white, false);
            textColor = Color.valueOf("#111");
        } else {
            profileIcon.pseudoClassStateChanged(selected_black, false);
            profileIcon.pseudoClassStateChanged(selected_white, true);
            textColor = Color.valueOf("#eee");
        }
        profileIcon.setTextFill(textColor);
        profileIcon.setBackground(new Background(new BackgroundFill(profileColor, new CornerRadii(100), Insets.EMPTY)));
    }

    private void updateMiniGoalBoxes() {
        List<GoalModel> topGoalModels = stonksObs.getTopGoals();
        
        if(topGoalModels.isEmpty()){
            lblNoGoalsToShow.setVisible(true);
        }else{
            lblNoGoalsToShow.setVisible(false);
        }
        
        int i = 0;
        for(i = 0; i < topGoalModels.size(); i++){
            topGoals.get(i).getRoot().setVisible(true);
            topGoals.get(i).setName(topGoalModels.get(i).getName());
            topGoals.get(i).setProgress(topGoalModels.get(i).getProgress());
        }
        for(; i < 5; i++){
            topGoals.get(i).getRoot().setVisible(false);
        }
    }
}
