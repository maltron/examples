//
//  PlayerDetailViewController.m
//  TeamPlayerCoreDataExample2
//
//  Created by Mauricio Leal on 1/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PlayerDetailViewController.h"
#import "Player.h"
#import "AppDelegate.h"
#import "TextFieldCell.h"

@interface PlayerDetailViewController ()
@property (nonatomic, strong) UILabel *labelName;
@property (nonatomic, strong) UITextField *textName;
@property (nonatomic, assign) CGFloat maxWidth;

@property (nonatomic, strong) UIBarButtonItem *buttonCancel, *buttonSave;

@end

@implementation PlayerDetailViewController
@synthesize teamSelected = _teamSelected;
@synthesize playerSelected = _playerSelected;

-(id)init {
    self = [super init];
    if(self) {
        _labelName = [self createLabel:@"Name"];
        _textName = [self createTextField:nil placeHolder:@"John Doe"];
        
        NSArray *array = [NSArray arrayWithObjects:_labelName, nil];
        _maxWidth = [self calculateMaxWidth:array];
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonCancel = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(actionCancel:)];
    _buttonSave = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemSave target:self action:@selector(actionSave:)];
    [self.navigationItem setLeftBarButtonItem:_buttonCancel];
    [self.navigationItem setRightBarButtonItem:_buttonSave];
}

-(void)actionCancel:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)actionSave:(id)sender {
    if(![self validatePlayer]) return;
    
    Player *player = [self hasPlayer] ? [self playerSelected] : [[AppDelegate instance] newPlayer];
//    if([self hasPlayer]) player = [self playerSelected];
//    else {
//        NSEntityDescription *entityPlayer = [NSEntityDescription entityForName:@"Player" inManagedObjectContext:[self.teamSelected managedObjectContext]];
//        player = (Player *)[[NSManagedObject alloc] initWithEntity:entityPlayer insertIntoManagedObjectContext:[self.teamSelected managedObjectContext]];
//    }
    [player setName:[self.textName text]];
    [self.teamSelected addPlayersObject:player];

    [[AppDelegate instance] saveContext];
//    NSManagedObjectContext *context = [self.teamSelected managedObjectContext];
//    NSError *error = nil;
//    if([context hasChanges] && ![context save:&error]) {
//        NSLog(@"### [PlayerDetailViewController actionSave] Error:%@, %@", error, [error userInfo]);
//        abort();
//    }
    
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)setPlayerSelected:(Player *)playerSelected {
    _playerSelected = playerSelected;
    
    [self.textName setText:[playerSelected name]];
    [self.navigationItem setTitle:@"Player"];
}

-(void)newPlayer {
    [self.textName setText:nil];
    [self setPlayerSelected:nil];
    [self.navigationItem setTitle:@"New Player"];
}

-(BOOL)validatePlayer {
    NSString *value = [self.textName text];
    AppDelegate *delegate = [AppDelegate instance];
    if(!value) {
        [[delegate alertTitle:@"Invalid"
                  withMessage:@"Player's name not valid"] show];
        return NO;
    }
    
    if([value length] == 0) {
        [[delegate alertTitle:@"Invalid"
                  withMessage:@"Player's name must be set"] show];
        return NO;
    }
    
    if([value length] == PLAYER_LENGTH_NAME) {
        [[delegate alertTitle:@"Invalid"
                  withMessage:@"Player's name must be less than 30 characters"] show];
        return NO;
    }
    
    if([delegate playerAlreadyExist:[self playerSelected] withName:value within:[self teamSelected]]) {
        [[delegate alertTitle:@"Already Exist"
                  withMessage:@"Team's name already exist"] show];
        return NO;
    }
    
    return YES;
}

-(BOOL)hasPlayer {
    return [self playerSelected];
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
