//
//  MyViewController.m
//  HelloWorld
//
//  Created by Mauricio Leal on 1/11/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PersonViewController.h"
#import "PersonDetailViewController.h"
#import "AutoLayout.h"
#import "AppDelegate.h"
#import "Person.h"

@interface PersonViewController()
@property (nonatomic, strong) NSFetchedResultsController *fetchedResultsController;
@property (nonatomic, strong) UIBarButtonItem *buttonFetch, *buttonAdd;
@property (nonatomic, strong) PersonDetailViewController *detail;

@end

@implementation PersonViewController
@synthesize detail = _detail;

-(void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonAdd = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(actionAdd:)];
    _buttonFetch = [[UIBarButtonItem alloc] initWithTitle:@"Fetch" style:UIBarButtonItemStylePlain target:self action:@selector(actionFetch:)];
    [self.navigationItem setLeftBarButtonItem:_buttonFetch];
    [self.navigationItem setRightBarButtonItem:_buttonAdd];
    [self.navigationItem setTitle:@"Person"];
}

-(void)actionAdd:(id)sender {
    NSLog(@"[PersonViewController actionAdd]");
    PersonDetailViewController *detail = [self detail];
    [detail newPerson];
    [self.navigationController pushViewController:detail animated:YES];
}

-(void)actionFetch:(id)sender {
    NSLog(@"[PersonViewController actionFetch]");
    
    AppDelegate *delegate = [AppDelegate instance];
    
    NSURL *url = [delegate url];
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url];
    NSLog(@"[PersonViewController actionFetch] REQUEST:<GET> %@", url);
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
    NSURLSessionDataTask *task = [[delegate urlSession] dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
        [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        NSLog(@"[PersonViewController actionFetch] Response:%ld", (unsigned long)[response statusCode]);
        
        NSLog(@"[PersonViewController actionFetch] URL:%@", url);
        if(error) {
            [delegate alertAsyncNetworkProblem];
            
        } else if([response statusCode] == 200) {
            NSLog(@"[PersonViewController actionFetch] OK"); // All data from server is here

            // Start deleting the local information first
            NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
            [fetchRequest setEntity:[delegate entityPerson]];
            
            NSError *errorFetchRequest = nil;
            NSArray *result = [[delegate managedObjectContext] executeFetchRequest:fetchRequest
                                                                             error:&errorFetchRequest];
            if(errorFetchRequest) {
                [delegate alertAsyncTitle:@"Sync Error" withMessage:@"Unable to fetch local objects"];
                return;
            }
            
            NSLog(@"[PersonViewController actionFetch] Deleting Local %lu person", (unsigned long)[result count]);
            for(Person *person in result)
                [[delegate managedObjectContext] deleteObject:person];
//                NSLog(@"Result:%@ XML:%@", [person class], [person toXML]);
            
            NSLog(@"[PersonViewController actionFetch] Saving Remote objects");
            NSError *errorJSON = nil;
            id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&errorJSON];
            if(errorJSON) [delegate alertAsyncJSONProblem];
            else if([json isKindOfClass:[NSArray class]]) {
                NSLog(@"[PersonViewController actionFetch] JSON is NSArray");
                NSArray *all = (NSArray *)json;
                for(NSDictionary *eachPerson in all) {
                    Person *newPerson = [delegate newPerson];
                    [newPerson parseJSONfromDictionary:eachPerson];
                    [delegate saveContext];
                }
                
            } else if([json isKindOfClass:[NSDictionary class]]) {
                NSLog(@"[PersonViewController actionFetch] JSON is NSDictionary");
                NSDictionary *all = (NSDictionary *)json;
                
            }
            
            
            
            
        } else { // SERVER PROBLEMS
            [delegate alertAsyncServerProblem];
        }
        
    }];
    [task resume];
//    
//    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
//    [fetchRequest setEntity:[delegate entityPerson]];
    
    
}

-(PersonDetailViewController *)detail {
    if(_detail) return _detail;
    
    _detail = [[PersonDetailViewController alloc] init];
    return _detail;
}

// TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE
//  TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE TABLE DELEGATE

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self.detail editPerson:[self.fetchedResultsController objectAtIndexPath:indexPath]];
    [self.navigationController pushViewController:self.detail animated:YES];
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@"[PersonViewController commitEditingStyle:forRowAtIndexPath]");
    
    if(editingStyle == UITableViewCellEditingStyleDelete) {
        AppDelegate *delegate = [AppDelegate instance];
        NSManagedObjectContext *context = [self.fetchedResultsController managedObjectContext];
        Person *person = (Person *)[self.fetchedResultsController objectAtIndexPath:indexPath];
        
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:
                                        [delegate urlDelete:person]];
        [request setHTTPMethod:@"DELETE"];

        NSLog(@"[PersonViewController commitEditingStyle:forRowAtIndexPath] REQUESTING...");
        [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
        NSURLSessionDataTask *task = [[delegate urlSession] dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
            [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
            
            NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
            NSLog(@"[PersonViewController commitEditingStyle:forRowAtIndexPath] Response:%ld", (unsigned long)[response statusCode]);
            
            if(error) {
                [delegate alertAsyncNetworkProblem];
             
            } else if([response statusCode] >= 200 && [response statusCode] < 300) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    NSLog(@"[PersonViewController commitEditingStyle:forRowAtIndexPath] Deleting Remote sucessfully. Deleting locally");
                    
                    // Successfully deleted
                    NSError *errorDelete = nil;
                    [context deleteObject:person];
                    if(![context save:&errorDelete]) {
                        NSLog(@"### [PersonViewController commitEditingStyle:forRowAtIndexPath] Error:%@, %@",errorDelete, [errorDelete userInfo]);
                    }
                });
                
            } else {
                [delegate alertAsyncServerProblem];
            }

            
        }];
        [task resume];
        
    }
}

-(void)controller:(NSFetchedResultsController *)controller didChangeObject:(id)anObject atIndexPath:(NSIndexPath *)indexPath forChangeType:(NSFetchedResultsChangeType)type newIndexPath:(NSIndexPath *)newIndexPath {
    NSLog(@"[PersonViewController didChangeObject:atIndexPath:forChangeType:newIndexPath] Type:%lu", type);
    
    UITableView *tableView = self.tableView;
    switch(type) {
        case NSFetchedResultsChangeInsert:
            NSLog(@"[PersonViewController didChangeObject:atIndexPath:forChangeType:newIndexPath] CHANGE INSERT");
                [tableView insertRowsAtIndexPaths:@[newIndexPath] withRowAnimation:UITableViewRowAnimationFade];
            break;
            
        case NSFetchedResultsChangeDelete:
            NSLog(@"[PersonViewController didChangeObject:atIndexPath:forChangeType:newIndexPath] CHANGE DELETE");
                [tableView deleteRowsAtIndexPaths:@[indexPath]
                             withRowAnimation:UITableViewRowAnimationFade];
            break;
            
        case NSFetchedResultsChangeUpdate:
            NSLog(@"[PersonViewController didChangeObject:atIndexPath:forChangeType:newIndexPath] CHANGE UPDATE");
                [self configureCell:[tableView cellForRowAtIndexPath:indexPath] atIndexPath:indexPath];
            break;
            
        case NSFetchedResultsChangeMove:
            NSLog(@"[PersonViewController didChangeObject:atIndexPath:forChangeType:newIndexPath] CHANGE MOVE");
                [tableView deleteRowsAtIndexPaths:@[indexPath]
                             withRowAnimation:UITableViewRowAnimationFade];
                [tableView insertRowsAtIndexPaths:@[newIndexPath] withRowAnimation:UITableViewRowAnimationFade];
            break;
    }
}


// ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT
//  ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT ENTITY PERSISTENT

-(NSFetchedResultsController *)fetchedResultsController {
    if(_fetchedResultsController) return _fetchedResultsController;
    
    AppDelegate *delegate = [AppDelegate instance];
    
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[delegate entityPerson]];
    [fetchRequest setFetchBatchSize:20];
    
    NSSortDescriptor *sortFirstName = [[NSSortDescriptor alloc] initWithKey:@"firstName" ascending:YES];
    NSSortDescriptor *sortLastName = [[NSSortDescriptor alloc] initWithKey:@"lastName" ascending:YES];
    [fetchRequest setSortDescriptors:@[sortFirstName, sortLastName]];
    
    NSFetchedResultsController *temp = [[NSFetchedResultsController alloc] initWithFetchRequest:fetchRequest managedObjectContext:[delegate managedObjectContext] sectionNameKeyPath:nil cacheName:@"CACHE_PERSON"];
    temp.delegate = self;
    self.fetchedResultsController = temp;
    
    NSError *error = nil;
    if(![self.fetchedResultsController performFetch:&error]) {
        NSLog(@"### [MyViewController fetchedResultsController] Error:%@, %@", error, [error userInfo]);
        abort();
    }
    
    return _fetchedResultsController;
}

-(void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    Person *person = (Person *)[self.fetchedResultsController objectAtIndexPath:indexPath   ];
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@ %@", [person firstName],
                           [person lastName]];
}

@end
