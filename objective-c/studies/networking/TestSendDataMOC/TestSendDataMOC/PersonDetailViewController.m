//
//  PersonDetailViewController.m
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/18/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PersonDetailViewController.h"
#import "PersonCRUD.h"
#import "Person.h"
#import "TextFieldCell.h"
#import "AppDelegate.h"

@interface PersonDetailViewController ()
@property (nonatomic, strong) NSNumber *personID;
@property (nonatomic, strong) UILabel *labelFirstName, *labelLastName;
@property (nonatomic, strong) UITextField *textFirstName, *textLastName;
@property (nonatomic, assign) CGFloat maxWidth;
@property (nonatomic, strong) PersonCRUD *crud;

@property (nonatomic, strong) UIBarButtonItem *buttonCancel, *buttonSave;

@end

@implementation PersonDetailViewController
@synthesize crud = _crud;
@synthesize personID = _personID;

-(id)initWithCRUD:(PersonCRUD *)crud {
    self = [super init];
    if(self) {
        _crud = crud;
        _labelFirstName = [self createLabel:@"First Name"];
        _textFirstName = [self createTextField:nil placeHolder:@"John"];
        _labelLastName = [self createLabel:@"Last Name"];
        _textLastName = [self createTextField:nil placeHolder:@"Doe"];
        
        NSArray *array = [NSArray arrayWithObjects:_labelFirstName, _labelLastName, nil];
        _maxWidth = [self calculateMaxWidth:array];
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonCancel = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(actionCancel:)];
    _buttonSave = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemSave target:self action:@selector(actionSave:)];
    [self.navigationItem setRightBarButtonItem:_buttonSave];
    [self.navigationItem setLeftBarButtonItem:_buttonCancel];
}

-(void)actionCancel:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)actionSave:(id)sender {
    // Validate
    if(![self validatePerson]) return;
    
    AppDelegate *delegate = [AppDelegate instance];
    // Check if this name already exist on the server
    NSString *firstName = [self.textFirstName text];
    NSString *lastName = [self.textLastName text];
    
    [self.crud findByFirst:firstName andLastName:lastName completionHandler:^(NSData *data, NSHTTPURLResponse *response, NSError *error) {
        NSLog(@">>> [PersonDetail actionSave] Response:%li",(long)[response statusCode]);
        
        if(error) { // Network Problem
            [delegate alertAsyncNetworkProblem];
            
        } else if([response statusCode] >= 200 && [response statusCode] < 300) {
            NSLog(@"### [PersonDetail actionSave] PERSON ALREADY EXISTS");
            dispatch_async(dispatch_get_main_queue(), ^{
                [[delegate alertTitle:@"Already Exist"
                          withMessage:[NSString
                    stringWithFormat:@"%@ %@ was found", firstName, lastName]]
                 show];
            });
            
        } else if([response statusCode] >= 400 && [response statusCode] < 500) {
            NSLog(@">>> [PersonDetail actionSave] Didn't find. It's a good thing");
            [self saveForGood:[self personID] firstName:firstName lastName:lastName];
            
        } else { // Server Problem
            [delegate alertAsyncServerProblem];
        }
        
    }];
}

-(void)saveForGood:(NSNumber *)iD firstName:(NSString *)firstName lastName:(NSString *)lastName {
    NSLog(@">>> [PersonDetail saveForGood]");
    AppDelegate *delegate = [AppDelegate instance];
    
    [self.crud save:iD firstName:firstName andLastName:lastName completionHandler:^(NSData *data, NSHTTPURLResponse *response, NSError *error) {
        NSLog(@">>> [PersonDetail saveForGood]");
        
        if(error) { // Network Problem
            [delegate alertAsyncNetworkProblem];
        } else if([response statusCode] >= 200 && [response statusCode] < 300) {
            NSLog(@">>> [PersonDetail saveForGood] SUCCESS");
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.navigationController popViewControllerAnimated:YES];
            });
            
        } else { // Server Problem
            [delegate alertAsyncServerProblem];
        }
        
    }];
}

-(BOOL)isNew {
    return _personID == nil;
}

-(void)newPerson {
    [self setPersonID:nil]; // It will indicate a INSERT
    [self.textFirstName setText:@""];
    [self.textFirstName becomeFirstResponder];
    [self.textLastName setText:@""];
    
    [self.navigationItem setTitle:@"New Person"];
}

-(void)editPerson:(Person *)person {
    [self setPersonID:[person iD]];
    [self.textFirstName setText:[person firstName]];
    [self.textFirstName becomeFirstResponder];
    [self.textLastName setText:[person lastName]];
    
    [self.navigationItem setTitle:[NSString stringWithFormat:@"[%@]:%@ %@",
                                   [person iD], [person firstName], [person lastName]]];
}

-(BOOL)validatePerson {
    AppDelegate *delegate = [AppDelegate instance];
    
    NSString *value = [self.textFirstName text];
    if([value length] == 0) {
        [[delegate alertTitle:@"Invalid"
                 withMessage:@"First Name cannot be empty"] show];
        return NO;
    }
    
    value = [self.textLastName text];
    if([value length] == 0) {
        [[delegate alertTitle:@"Invalid"
                 withMessage:@"Last Name cannot be empty"] show];
        return NO;
    }
    
    return YES;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE
//   TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE TABLE DATASOURCE
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 2;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    TextFieldCell *cell = nil;
    if(indexPath.row == 0) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"cellFirstName"];
        if(!cell) cell = [[TextFieldCell alloc] initWithLabel:self.labelFirstName andTextField:self.textFirstName withMaxWidth:self.maxWidth reuseIdentifier:@"cellFirstName"];
        
    } else if(indexPath.row == 1) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"cellLastName"];
        if(!cell) cell = [[TextFieldCell alloc] initWithLabel:self.labelLastName andTextField:self.textLastName withMaxWidth:self.maxWidth reuseIdentifier:@"cellLastName"];
    }
    
    return cell;
}

@end
