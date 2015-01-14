//
//  MyViewController.m
//  HelloWorld
//
//  Created by Mauricio Leal on 1/11/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "MyViewController.h"
#import "AutoLayout.h"
#import "Person.h"
#import "AppDelegate.h"

@interface MyViewController() <UITableViewDataSource>
@property (nonatomic, strong) UIBarButtonItem *buttonRequest, *buttonRight;
@property (nonatomic, strong) NSFetchedResultsController *fetchedResultsController;

@end

@implementation MyViewController
@synthesize fetchedResultsController = _fetchedResultsController;

-(void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonRequest = [[UIBarButtonItem alloc] initWithTitle:@"Request" style:UIBarButtonItemStylePlain target:self action:@selector(actionRequest:)];
    _buttonRight = [[UIBarButtonItem alloc] initWithTitle:@"Right" style:UIBarButtonItemStylePlain target:self action:@selector(actionRight:)];
    [self.navigationItem setLeftBarButtonItem:_buttonRequest];
    [self.navigationItem setRightBarButtonItem:_buttonRight];
    [self.navigationItem setTitle:@"Persons"];
}

-(void)actionRequest:(id)sender {
    NSLog(@"[MyViewController actionLeft] START");
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:[NSURL URLWithString:@"http://localhost:8080/samplejson/rest/resource"]];
    
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration ephemeralSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:configuration];
    NSURLSessionDataTask *task = [session dataTaskWithRequest:request completionHandler:
                                  ^(NSData *data, NSURLResponse *r, NSError *error) {
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        NSDate *start = [NSDate date];
        if(!error && [response statusCode] == 200) {
            dispatch_async(dispatch_get_main_queue(), ^{
                NSLog(@"[MyViewController actionLeft] RESPONSE START: %@", start);
                [self handleFetch:data];
                NSLog(@"[MyViewController actionLeft] RESPONSE START: %f", [start timeIntervalSinceNow]);
            });
            
        } else if(error) {
            NSLog(@"### [MyViewController actionLeft] Error:%@", error);
        }
        
        
    }]; [task resume];
    
    NSLog(@"[MyViewController actionLeft] END");
}

-(void)handleFetch:(NSData *)data {
    NSLog(@"[MyViewController handleFetch] START");
    AppDelegate *delegate = [AppDelegate instance];
    Person *newPerson = [delegate newPerson];
    [newPerson parseJSON:data];
    [delegate saveContext];
    NSLog(@"[MyViewController handleFetch] END");
}

-(void)actionRight:(id)sender {
    NSLog(@"[MyViewController actionRight]");
    
}

// ENTITY PERSON ENTITY PERSON ENTITY PERSON ENTITY PERSON ENTITY PERSON ENTITY PERSON
//   ENTITY PERSON ENTITY PERSON ENTITY PERSON ENTITY PERSON ENTITY PERSON ENTITY PERSON

-(NSFetchedResultsController *)fetchedResultsController {
    if(_fetchedResultsController) return _fetchedResultsController;
    
    AppDelegate *delegate = [AppDelegate instance];
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[delegate entityPerson]];
    [fetchRequest setFetchBatchSize:20];
    
    NSSortDescriptor *sortFirstName = [NSSortDescriptor sortDescriptorWithKey:@"firstName"
                                                                        ascending:YES];
//    NSSortDescriptor *sortLastName = [NSSortDescriptor sortDescriptorWithKey:@"lastName"
//                                                                   ascending:YES];
    [fetchRequest setSortDescriptors:@[sortFirstName]];
    
    NSFetchedResultsController *tempController = [[NSFetchedResultsController alloc] initWithFetchRequest:fetchRequest managedObjectContext:[delegate managedObjectContext] sectionNameKeyPath:nil cacheName:@"CACHE_PERSON"];
    [tempController setDelegate:self];
    self.fetchedResultsController = tempController;
    
    NSError *error = nil;
    if(![self.fetchedResultsController performFetch:&error]) {
        NSLog(@"### [MyViewController fetchedResultsController] Error:%@", error);
        abort();
    }
    
    return _fetchedResultsController;
}

-(void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    Person *person = (Person *)[[self fetchedResultsController] objectAtIndexPath:indexPath];
    cell.textLabel.text = [NSString stringWithFormat:@"%@ %@",
                           [person firstName], [person lastName]];
}

@end
