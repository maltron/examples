//
//  TeamDetailViewController.m
//  TeamPlayerCoreDataExample2
//
//  Created by Mauricio Leal on 1/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "TeamDetailViewController.h"
#import "Team.h"
#import "TextFieldCell.h"
#import "AppDelegate.h"

@interface TeamDetailViewController ()
@property (nonatomic, strong) UILabel *labelName;
@property (nonatomic, strong) UITextField *textName;
@property (nonatomic, assign) CGFloat maxWidth;

@property (nonatomic, strong) UIBarButtonItem *buttonCancel, *buttonSave;

@end

@implementation TeamDetailViewController
@synthesize teamSelected = _teamSelected;

-(id)init {
    self = [super init];
    if(self) {
        _labelName = [self createLabel:@"Name"];
        _textName = [self createTextField:nil placeHolder:@"The Giants"];
        
        NSArray *array = [NSArray arrayWithObjects:_labelName, nil];
        _maxWidth = [self calculateMaxWidth:array];
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonCancel = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(cancel:)];
    _buttonSave = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemSave target:self action:@selector(saveTeam:)];
    [self.navigationItem setLeftBarButtonItem:_buttonCancel];
    [self.navigationItem setRightBarButtonItem:_buttonSave];
}

-(void)cancel:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)saveTeam:(id)sender {
    // Step #1: Validate
    if(![self validateTeam]) return;
    
    // Step #2: Save all informations
    Team *team = [self hasTeam] ? [self teamSelected] :
                                [[AppDelegate instance] newTeam];
    // Step #3: Populate
    [team setName:[self.textName text]];
    
    // Save it
    [[AppDelegate instance] saveContext];
    
    [self.navigationController popViewControllerAnimated:YES];
}

-(BOOL)validateTeam {
    AppDelegate *delegate = [AppDelegate instance];
    NSString *value = [self.textName text];
    if(!value) {
        [[delegate alertTitle:@"Invalid"
                  withMessage:@"Team's name is not invalid"] show];
        return NO;
    }
    
    if([value length] == 0) {
        [[delegate alertTitle:@"Invalid"
                  withMessage:@"Team's name must be set"] show];
        return NO;
    }
    
    if([value length] > TEAM_LENGTH_NAME) {
        [[delegate alertTitle:@"Invalid"
                  withMessage:@"Team's name must be less than 30 characters"] show];
        return NO;
    }
    
    if([delegate teamAlreadyExist:[self teamSelected] withName:value]) {
        [[delegate alertTitle:@"Already Exist"
                  withMessage:@"This Team's name already exist"] show];
        return NO;
    }
    
    return YES;
}

-(void)setTeamSelected:(Team *)teamSelected {
    _teamSelected = teamSelected;
    
    [self.textName setText:[teamSelected name]];
    [self.navigationItem setTitle:@"Team"];
}

-(void)newTeam {
    [self.textName setText:nil];
    [self.navigationItem setTitle:@"New Team"];
    
    [self setTeamSelected:nil]; // Indicates a new Record must be create
}

-(BOOL)hasTeam {
    return [self teamSelected];
}

// TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE
//   TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    TextFieldCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    if(!cell) cell = [[TextFieldCell alloc] initWithLabel:_labelName andTextField:_textName withMaxWidth:_maxWidth reuseIdentifier:@"cell"];
    
    return cell;
}



@end
