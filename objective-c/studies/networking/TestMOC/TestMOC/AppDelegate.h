//
//  AppDelegate.h
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

#define SERVER  @"http://localhost:8080/person/rest/resource/"

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *mainContext;
@property (readonly, strong, nonatomic) NSURLSession *session;

-(void)saveContext;

+(AppDelegate *)instance;
-(UIAlertView *)alertViewTitle:(NSString *)title withMessage:(NSString *)message;
-(void)alertAsyncNetworkProblem;
-(void)alertAsyncServerProblem;
-(void)alertAsyncJSONProblem;

-(NSURL *)url;
-(NSURL *)urlFindFirst:(NSString *)firstName andLastName:(NSString *)lastName;
-(NSURL *)urlDelete:(NSNumber *)iD;


@end

