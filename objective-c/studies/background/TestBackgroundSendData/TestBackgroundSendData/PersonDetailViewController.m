//
//  PersonDetailViewController.m
//  TestBackgroundSendData
//
//  Created by Mauricio Leal on 2/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PersonDetailViewController.h"
#import "TextFieldCell.h"
#import "Person.h"
#import "AppDelegate.h"

@interface PersonDetailViewController()
@property (nonatomic, strong) UILabel *labelFirstName;
@property (nonatomic, strong) UITextField *textFirstName;
@property (nonatomic, strong) UILabel *labelLastName;
@property (nonatomic, strong) UITextField *textLastName;
@property (nonatomic, assign) CGFloat maxWidth;
@property (nonatomic, strong) NSString *method; // It can be either POST or PUT

@property (nonatomic, strong) UIBarButtonItem *buttonCancel, *buttonSave;

@end

@implementation PersonDetailViewController
@synthesize personID = _personID;

-(id)init {
    self = [super init];
    if(self) {
        _labelFirstName = [self createLabel:@"First Name"];
        _textFirstName = [self createTextField:nil placeHolder:@"John"];
        _labelLastName = [self createLabel:@"Last Name"];
        _textLastName = [self createTextField:nil placeHolder:@"Doe"];
        
        NSArray *array = [NSArray arrayWithObjects:_labelFirstName, _labelLastName, nil];
        _maxWidth = [self calculateMaxWidth:array];
    }
    
    return self;
}

-(void)newPerson {
    [self setPersonID:nil]; // It will indicate a INSERT
    [self.textFirstName setText:@""];
    [self.textFirstName becomeFirstResponder];
    [self.textLastName setText:@""];
    
    [self setMethod:@"POST"]; // REST POST = SQL INSERT
    
    [self.navigationItem setTitle:@"New Person"];
}

-(void)editPerson:(Person *)person {
    [self setPersonID:[person iD]];
    [self.textFirstName setText:[person firstName]];
    [self.textFirstName becomeFirstResponder];
    [self.textLastName setText:[person lastName]];
    
    [self setMethod:@"PUT"]; // REST PUT = SQL UPDATE
    
    [self.navigationItem setTitle:[NSString stringWithFormat:@"[%@]:%@ %@",
    [person iD], [person firstName], [person lastName]]];
}

-(BOOL)hasPerson {
    return [self personID];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonCancel = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(actionCancel:)];
    _buttonSave = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemSave target:self action:@selector(checkBeforeSaving:)];
    [self.navigationItem setRightBarButtonItem:_buttonSave];
    [self.navigationItem setLeftBarButtonItem:_buttonCancel];
}

-(void)actionCancel:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)checkBeforeSaving:(id)sender {
    NSLog(@"[PersonDetailViewController checkBeforeSaving]");
    // Validate
    if(![self validatePerson]) return;
    AppDelegate *delegate = [AppDelegate instance];
    
    NSString *firstName = [self.textFirstName text];
    NSString *lastName = [self.textLastName text];
    
//    Person *person = [self hasPerson] ? self.personSelected : [delegate newPerson];
//    [person setFirstName:[self.textFirstName text]];
//    [person setLastName:[self.textLastName text]];
    
    // Before finally saving it, try to remote validated
    // this First and Last Name
    NSURL *url = [delegate urlFindFirst:firstName andLastName:lastName];
    if(!url) NSLog(@"### [PersonDetailViewController checkBeforeSaving] URL is NULL");
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url];
    
    NSLog(@"[PersonDetailViewController checkBeforeSaving] REQUEST:%@", url);
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
    NSURLSessionDataTask *task = [[delegate urlSession] dataTaskWithRequest:request
    completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
        [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        NSLog(@"[PersonDetailViewController checkBeforeSaving] Response %ld", (unsigned long)[response statusCode]);
        if(error) {
            NSLog(@"[PersonDetailViewController checkBeforeSaving] ERROR:%@", error);
            [delegate alertAsyncNetworkProblem];
            
        } else if([response statusCode] == 200) { // Found a name on the server
            if([self.method isEqualToString:@"POST"]) { // POST ????
                // Message: Person already exists
                [self personAlreadyExistMessage:firstName andLastName:lastName];
                
            } else if([self.method isEqualToString:@"PUT"]) { // PUT ????
                Person *tempPerson = [[Person alloc] init];
                [tempPerson parseJSON:data];
                if([self personID] == [tempPerson iD]) {
                    // It's the same person
                    [self saveForGood:firstName andLastName:lastName];
                    
                } else {
                    // Message: Person alredy exists
                    [self personAlreadyExistMessage:[self.textFirstName text] andLastName:[self.textLastName text]];
                }
                
                
            }
            
        } else { // ALL GOOD. Save locally and remotelly
            NSLog(@"[PersonDetailViewController checkBeforeSaving] ALL GOOD. Saving...");
            [self saveForGood:firstName andLastName:lastName];
        }
    }];
    [task resume];
}

-(void)saveForGood:(NSString *)firstName andLastName:(NSString *)lastName {
    NSLog(@"[PersonDetailViewController saveForGood]");
    AppDelegate *delegate = [AppDelegate instance];
    Person *person = [self hasPerson] ? [[Person alloc] init] : [delegate newPerson];
    if([self hasPerson]) [person setID:[self personID]];
    [person setFirstName:firstName];
    [person setLastName:lastName];
    
    NSURL *url = [delegate url];
    if(!url) NSLog(@"### [PersonDetailViewController saveForGood] URL is NULL");
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    [request setHTTPMethod:[self method]];
    
    NSLog(@"[PersonDetailViewController actionSave] REQUEST:%@", url);
    NSURLSessionUploadTask *task = [[delegate urlSession] uploadTaskWithRequest:request
                                                                       fromData:[person json]
    completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        NSLog(@"[PersonDetailViewController actionSave] Response:%ld", (long)[response statusCode]);
        if(error) {
            [delegate alertAsyncNetworkProblem];
            
        } else if([response statusCode] >= 200 && [response statusCode] < 300) {
            NSLog(@"[PersonDetailViewController actionSave] Response 200-300");
                  
            // Sucessfully saved on the server. Parse the content into the current
            // instance of Person. This time, it will save it with an ID given
            // by the Server.
            [person parseJSON:data];
            NSLog(@"[PersonDetailViewController actionSave] Person:%@", [person toXML]);
            // and then....save it locally
            [delegate saveContext];
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.navigationController popViewControllerAnimated:YES];
            });
            
            
        } else { // Although communication was possible, something went wrong
            NSLog(@"[PersonDetailViewController actionSave] Response 400~");
            dispatch_async(dispatch_get_main_queue(), ^{
                [[delegate alertTitle:@"Server Problem"
                          withMessage:@"Unable to understand Server's response"] show];
                [self.navigationController popViewControllerAnimated:YES];
            });
            
        }
        
        // Closes the Network Indicator
        [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    }];
    [task resume];
}

-(void)personAlreadyExistMessage:(NSString *)firstName andLastName:(NSString *)lastName {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
        [[[AppDelegate instance] alertTitle:@"Invalid"
                  withMessage:[NSString stringWithFormat:@"The name \"%@ %@\" already exist on the Server.", firstName, lastName]] show];
    });
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(BOOL)validatePerson {
    AppDelegate *delegate = [AppDelegate instance];
    
    NSString *value = [self.textFirstName text];
    if([value length] == 0) {
        [delegate alertTitle:@"Invalid"
                 withMessage:@"First Name cannot be empty"];
        return NO;
    }
    
    value = [self.textLastName text];
    if([value length] == 0) {
        [delegate alertTitle:@"Invalid"
                 withMessage:@"Last Name cannot be empty"];
        return NO;
    }
    
    return YES;
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
