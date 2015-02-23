//
//  PersonDetailViewController.m
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PersonDetailViewController.h"
#import "TextFieldCell.h"
#import "Person.h"
#import "AppDelegate.h"
#import "PersonService.h"

@interface PersonDetailViewController ()
@property (nonatomic, strong) UILabel *labelFirstName, *labelLastName;
@property (nonatomic, strong) UITextField *textFirstName, *textLastName;
@property (nonatomic, assign) CGFloat maxWidth;

@property (nonatomic, strong) UIBarButtonItem *buttonCancel, *buttonSave;
@property (nonatomic, strong) PersonService *service;

@end

@implementation PersonDetailViewController
@synthesize service = _service;

-(id)init:(PersonService *)service {
    self = [super init];
    if(self) {
        _service = service;
        _labelFirstName = [self createLabel:@"First Name"];
        _textFirstName = [self createTextField:nil placeHolder:@"John"];
        _labelLastName = [self createLabel:@"Last Name"];
        _textLastName = [self createTextField:nil placeHolder:@"Doe"];
        _maxWidth = [self calculateMaxWidth:[NSArray arrayWithObjects:_labelFirstName, _labelLastName       , nil]];
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonCancel = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(actionCancel:)];
    _buttonSave =  [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemSave target:self action:@selector(actionSave:)];
    [self.navigationItem setLeftBarButtonItem:_buttonCancel];
    [self.navigationItem setRightBarButtonItem:_buttonSave];
}

-(void)actionCancel:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)actionSave:(id)sender {
    NSLog(@">>> [PersonDetailVC actionSave]");
    if(![self validatePerson]) return;
    AppDelegate *delegate = [AppDelegate instance];
    
    // First, check if the name does exist on the Server
    NSString *firstName = [self.textFirstName text];
    NSString *lastName = [self.textLastName text];
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
    [self.service findFirstName:firstName andLastName:lastName
              completionHandler:^(NSData *data, NSHTTPURLResponse *response, NSError *error) {
        [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
        
        if(error) { // Networking Problems
            [delegate alertAsyncNetworkProblem];
            
        } else if([self.service isSuccessfull:response]) {
            if([self hasPerson]) { // INSERT
                // FOUND IT. Unable to save it
                [self alertAsyncDuplicateFound:firstName andLastName:lastName];
                
            } else { // UPDATE
                Person *temp = [[Person alloc] init];
                [temp parseJSON:data];
                if([temp iD] == [self.personSelected iD]) {
                    // It's the same person. Nothing to worry about it
                    [self saveForGood:firstName andLastName:lastName];
                    
                } else {
                    // Different person. Unable to save it.
                    [self alertAsyncDuplicateFound:firstName andLastName:lastName];
                }
            }
            
        } else if([self.service isError:response]) {
            // NOT FOUND. It's a good thing
            [self saveForGood:firstName andLastName:lastName];
            
            
        } else if([self.service isServerError:response]) {
            [delegate alertAsyncServerProblem];
            
        }
    }];
}

-(void)alertAsyncDuplicateFound:(NSString *)firstName andLastName:(NSString *)lastName {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[[AppDelegate instance]
          alertViewTitle:@"Duplicate"
          withMessage:[NSString stringWithFormat:@"A person \'%@ %@\' already existe on the server", firstName, lastName]] show];
    });
}

-(void)saveForGood:(NSString *)firstName andLastName:(NSString *)lastName {
    AppDelegate *delegate = [AppDelegate instance];
    
    // IMPORTANT: It's *NOT* using the mainContext
    Person *person = [self hasPerson] ? [self personSelected] : [Person newPerson:[self.service personContext]];
    [person setFirstName:firstName];
    [person setLastName:lastName];
    
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
    [self.service save:person isInsert:![self hasPerson]
     completionHandler:^(NSData *data, NSHTTPURLResponse *response, NSError *error) {
         [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
        
         if(error) { // Network Problem
             [delegate alertAsyncNetworkProblem];
             
         } else if([self.service isSuccessfull:response]) {
             // GOOD. The whole PersonService will take care of saving locally.
             dispatch_async(dispatch_get_main_queue(), ^{
                [delegate saveContext]; // Is it REALLY necessary ??? WHY ????
                [self.navigationController popViewControllerAnimated:YES];
             });
             
         } else if([self.service isServerError:response]) {
             [delegate alertAsyncServerProblem];
         }
         
    }];
}

-(void)newPerson {
    [self setPersonSelected:nil];
    [self.textFirstName setText:@""]; [self.textFirstName becomeFirstResponder];
    [self.textLastName setText:@""];
    
    [self.navigationItem setTitle:@"New Person"];
    
    [self.textFirstName performSelectorOnMainThread:@selector(becomeFirstResponder) withObject:nil waitUntilDone:0.0];
}

-(void)editPerson:(Person *)person {
    [self setPersonSelected:person];
    [self.textFirstName setText:[person firstName]];
    [self.textFirstName becomeFirstResponder];
    [self.textLastName setText:[person lastName]];
    
    [self.navigationItem setTitle:[NSString stringWithFormat:@"%@:%@ %@",
        [person iD],
        [person firstName],
        [person lastName]]];
    [self.textFirstName performSelectorOnMainThread:@selector(becomeFirstResponder) withObject:nil waitUntilDone:0.0];
}

-(BOOL)hasPerson {
    return [self personSelected];
}

-(BOOL)validatePerson {
    AppDelegate *delegate = [AppDelegate instance];
    NSString *value = [self.textFirstName text];
    if(!value) {
        [[delegate alertViewTitle:@"Invalid"
                      withMessage:@"First Name is a mandatory information"] show];
        return NO;
    }
    
    if([value length] == 0) {
        [[delegate alertViewTitle:@"Invalid"
                      withMessage:@"First Name cannot be empty"] show];
        return NO;
    }
    
    if([value length] > 30) {
        [[delegate alertViewTitle:@"Invalid"
                      withMessage:@"First Name cannot be larger than 30 characters"] show];
        return NO;
    }
    
    value = [self.textLastName text];
    if(!value) {
        [[delegate alertViewTitle:@"Invalid"
                      withMessage:@"Last Name is a mandatory information"] show];
        return NO;
    }
    
    if([value length] == 0) {
        [[delegate alertViewTitle:@"Invalid"
                      withMessage:@"Last Name cannot be empty"] show];
        return NO;
    }
    
    if([value length] > 30) {
        [[delegate alertViewTitle:@"Invalid"
                      withMessage:@"Last Name cannot be larger than 30 characters"] show];
        return NO;
    }
    
    return YES;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE
//  TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE TABLE SOURCE

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 2;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    TextFieldCell *cell;
    if(indexPath.row == 0) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"cellFirst"];
        if(!cell) cell = [[TextFieldCell alloc] initWithLabel:self.labelFirstName andTextField:self.textFirstName withMaxWidth:self.maxWidth reuseIdentifier:@"cellFirst"];
        
    } else if(indexPath.row == 1) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"cellLast"];
        if(!cell) cell = [[TextFieldCell alloc] initWithLabel:self.labelLastName andTextField:self.textLastName withMaxWidth:self.maxWidth reuseIdentifier:@"cellLast"];
    }
    
    return cell;
}




@end
